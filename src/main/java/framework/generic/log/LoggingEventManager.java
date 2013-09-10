package framework.generic.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.spi.LoggingEvent;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import framework.generic.queue.QueueConsumer;
import framework.generic.util.FrameworkUtil;

/**
 * 将Queue中的log4j event写入数据库的消费者任务.
 * 
 * 即时阻塞的读取Queue中的事件,使用Jdbc批量写入模式.
 * 
 * <p>
 * framework-generic-mybatis
 * </p>
 * <p>
 * 项目名称：2013-广州扬基信息科技有限公司
 * </p>
 * 
 * @see framework.generic.mybatis.log.LoggingEventManager
 * @version 1.0, 2013-8-28 下午1:41:22
 * @author quanyongan
 */
public class LoggingEventManager extends QueueConsumer {

	protected String sql;
	protected int batchSize = 50;
	private String logSql;
	private String exceptionSql;
	protected static final String LEVEL_INFO = "INFO";
	protected static final String LEVEL_ERROR = "ERROR";

	protected List<LoggingEvent> eventsBuffer = new ArrayList<LoggingEvent>();
	protected NamedParameterJdbcTemplate jdbcTemplate;
	protected TransactionTemplate transactionTemplate;

	/**
	 * 批量读取事件数量, 默认为50.
	 */
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	/**
	 * 带Named Parameter的日志insert sql.
	 */
	public String getLogSql() {
		if (FrameworkUtil.isNullOrEmpty(logSql)) {
			logSql = "INSERT INTO COM_LOG(ACCOUNT_ID,IP_ADDRESS,APP_CODE,ACTION,MESSAGE,CREATE_DATE) values(:account_id,:ip_address,:app_code,:action,:message,:create_date)";
		}
		return logSql;
	}

	public void setLogSql(String logSql) {
		this.logSql = logSql;
	}

	/**
	 * 带Named Parameter的异常insert sql.
	 */
	public String getExceptionSql() {
		if (FrameworkUtil.isNullOrEmpty(exceptionSql)) {
			exceptionSql = "INSERT INTO COM_EXCEPTION_LOG(APP_CODE,CLASS_NAME,METHOD_NAME,LINE_NUM,MESSAGE,CREATE_DATE) VALUES(:app_code,:class_name,:method_name,:line_num,:message,:create_date)";
		}
		return exceptionSql;
	}

	public void setExceptionSql(String exceptionSql) {
		this.exceptionSql = exceptionSql;
	}

	/**
	 * 根据注入的DataSource创建jdbcTemplate.
	 */
	@Resource
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	/**
	 * 根据注入的PlatformTransactionManager创建transactionTemplate.
	 */
	@Resource
	public void setDefaultTransactionManager(PlatformTransactionManager defaultTransactionManager) {
		transactionTemplate = new TransactionTemplate(defaultTransactionManager);
	}

	/**
	 * 消息处理函数,将消息放入buffer,当buffer达到batchSize时执行批量更新函数.
	 */
	@Override
	protected void processMessage(Object message) {
		LoggingEvent event = (LoggingEvent) message;
		if (!event.getProperties().isEmpty()) {
			eventsBuffer.add(event);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("get event: {}", new LoggingEventWrapper(event).convertToString());
		}

		if (!eventsBuffer.isEmpty()) {
			if (LEVEL_INFO.equals(event.getLevel().toString().toUpperCase())) {
				this.sql = getLogSql();
			} else if (LEVEL_ERROR.equals(event.getLevel().toString().toUpperCase())) {
				this.sql = getExceptionSql();
			} else {
			}
			insertToDb();
		}
	}

	/**
	 * 将Buffer中的事件列表批量插入数据库.
	 */
	@SuppressWarnings("rawtypes")
	public void insertToDb() {
		try {
			// 分析事件列表, 转换为jdbc批处理参数.
			int i = 0;
			Map[] paramMapArray = new HashMap[eventsBuffer.size()];
			for (LoggingEvent event : eventsBuffer) {
				paramMapArray[i++] = parseEvent(event);
			}
			final SqlParameterSource[] batchParams = SqlParameterSourceUtils.createBatch(paramMapArray);

			// 执行批量插入,如果失败调用失败处理函数.
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					try {
						jdbcTemplate.batchUpdate(getActualSql(), batchParams);
						if (logger.isDebugEnabled()) {
							for (LoggingEvent event : eventsBuffer) {
								logger.debug("saved event: {}", new LoggingEventWrapper(event).convertToString());
							}
						}
					} catch (DataAccessException e) {
						status.setRollbackOnly();
						handleDataAccessException(e, eventsBuffer);
					}
				}
			});

			// 清除已完成的Buffer
			eventsBuffer.clear();
		} catch (Exception e) {
			logger.error("批量提交任务时发生错误.", e);
		}
	}

	/**
	 * 退出清理函数,完成buffer中未完成的消息.
	 */
	@Override
	protected void clean() {
		if (!eventsBuffer.isEmpty()) {
			insertToDb();
		}
		logger.debug("cleaned task {}", this);
	}

	/**
	 * 分析Event, 建立Parameter Map, 用于绑定sql中的Named Parameter.
	 */
	protected Map<String, Object> parseEvent(LoggingEvent event) {
		LoggingEventWrapper eventWrapper = new LoggingEventWrapper(event);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		// 公共
		parameterMap.put(LoggingEventConst.APP_CODE, eventWrapper.getAppId());
		parameterMap.put(LoggingEventConst.CREATE_DATE, eventWrapper.getCreateDate());
		parameterMap.put(LoggingEventConst.MESSAGE, eventWrapper.getMessage());

		// 日志
		parameterMap.put(LoggingEventConst.ACCOUNT_ID, eventWrapper.getAccountId());
		parameterMap.put(LoggingEventConst.IP, eventWrapper.getIpAddress());
		parameterMap.put(LoggingEventConst.ACTION, eventWrapper.getAction());

		// 异常
		parameterMap.put(LoggingEventConst.CLASS_NAME, eventWrapper.getClassName());
		parameterMap.put(LoggingEventConst.METHOD_NAME, eventWrapper.getMethodName());
		parameterMap.put(LoggingEventConst.LINE_NUM, eventWrapper.getLineNum());

		return parameterMap;
	}

	/**
	 * 可被子类重载的数据访问错误处理函数,如将出错的事件持久化到文件.
	 */
	protected void handleDataAccessException(DataAccessException e, List<LoggingEvent> errorEventBatch) {
		if (e instanceof DataAccessResourceFailureException) {
			logger.error("database connection error", e);
		} else {
			logger.error("other database error", e);
		}

		for (LoggingEvent event : errorEventBatch) {
			logger.error("event insert to database error, ignore it: " + new LoggingEventWrapper(event).convertToString(), e);
		}
	}

	/**
	 * 可被子类重载的sql提供函数,可对sql语句进行特殊处理，如日志表的表名可带日期后缀 LOG_2013-08-03.
	 */
	protected String getActualSql() {
		return sql;
	}

	/**
	 * 线程执行函数,阻塞获取消息并调用processMessage()进行处理.
	 */
	@Override
	public void run() {
		// 循环阻塞获取消息直到线程被中断.
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Object message = queue.take();
				processMessage(message);
			}
		} catch (InterruptedException e) {
			// Ignore.
		} finally {
			// 退出线程前调用清理函数.
			clean();
		}
	}
}
