package framework.generic.log;

import java.util.Date;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import framework.generic.util.FrameworkUtil;

/**
 * Log4j LoggingEvent的包装类, 提供默认的toString函数及更直观的属性访问方法.
 * 
 * framework-generic-mybatis 项目名称：2013-广州扬基信息科技有限公司
 * 
 * @see framework.generic.mybatis.log.LoggingEventWrapper
 * @version 1.0, 2013-8-28 下午1:29:45
 * @author quanyongan
 */
public class LoggingEventWrapper {
	public static final PatternLayout DEFAULT_PATTERN_LAYOUT = new PatternLayout("%d [%t] %-5p %c - %m");

	private final LoggingEvent event;

	public LoggingEventWrapper(LoggingEvent event) {
		this.event = event;
	}

	/**
	 * 使用默认的layoutPattern转换事件到日志字符串.
	 */
	public String convertToString() {
		return DEFAULT_PATTERN_LAYOUT.format(event);
	}

	/**
	 * 根据参数中的layoutPattern转换事件到日志字符串.
	 */
	public String convertToString(String layoutPattern) {
		return new PatternLayout(layoutPattern).format(event);
	}

	/**
	 * 时间戳
	 */
	public long getTimeStamp() {
		return event.getTimeStamp();
	}

	/**
	 * 创建时间 getCreateDate
	 */
	public Date getCreateDate() {
		return new Date(event.getTimeStamp());
	}

	/**
	 * 当前线程的名称
	 */
	public String getThreadName() {
		return event.getThreadName();
	}

	/**
	 * log4j的名称
	 */
	public String getLoggerName() {
		return event.getLoggerName();
	}

	/**
	 * log4J日志的级别
	 */
	public String getLevel() {
		return event.getLevel().toString();
	}

	/**
	 * 错误或者日志消息
	 */
	public String getMessage() {
		return (String) event.getMessage();
	}

	/**
	 * 类名称
	 */
	public String getClassName() {
		if (!FrameworkUtil.isNullOrEmpty(event.getProperty(LoggingEventConst.CLASS_NAME))) {
			return (String) event.getProperty(LoggingEventConst.CLASS_NAME);
		} else {
			return "";
		}
	}

	/**
	 * 方法名称
	 */
	public String getMethodName() {
		if (!FrameworkUtil.isNullOrEmpty(event.getProperty(LoggingEventConst.METHOD_NAME))) {
			return (String) event.getProperty(LoggingEventConst.METHOD_NAME);
		} else {
			return "";
		}
	}

	/**
	 * 错误行号
	 */
	public String getLineNum() {
		if (!FrameworkUtil.isNullOrEmpty(event.getProperty(LoggingEventConst.LINE_NUM))) {
			return String.valueOf(event.getProperty(LoggingEventConst.LINE_NUM));
		} else {
			return "";
		}
	}

	/**
	 * 应用程序的代码
	 */
	public String getAppId() {
		if (!FrameworkUtil.isNullOrEmpty(event.getProperty(LoggingEventConst.APP_CODE))) {
			return (String) event.getProperty(LoggingEventConst.APP_CODE);
		} else {
			return "";
		}
	}

	/**
	 * 用户账号信息
	 */
	public String getAccountId() {
		if (!FrameworkUtil.isNullOrEmpty(event.getProperty(LoggingEventConst.ACCOUNT_ID))) {
			return (String) event.getProperty(LoggingEventConst.ACCOUNT_ID);
		} else {
			return "";
		}
	}

	/**
	 * 操作的IP地址
	 */
	public String getIpAddress() {
		if (!FrameworkUtil.isNullOrEmpty(event.getProperty(LoggingEventConst.IP))) {
			return (String) event.getProperty(LoggingEventConst.IP);
		} else {
			return "";
		}
	}

	/**
	 * 操作的动作如(update,insert,delete)自已定义操作的动作
	 */
	public String getAction() {
		if (!FrameworkUtil.isNullOrEmpty(event.getProperty(LoggingEventConst.ACTION))) {
			return (String) event.getProperty(LoggingEventConst.ACTION);
		} else {
			return "";
		}
	}
}
