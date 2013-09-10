package framework.generic.log;

import java.util.Date;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import framework.generic.util.FrameworkConst;
import framework.generic.util.FrameworkUtil;

/**
 * 日志处理类 只提供info级别的日志
 * <p>
 * 项目名称：framework-generic
 * </p>
 * <p>
 * 版权：2012-广州扬基信息科技有限公司
 * </p>
 * 
 * @see framework.generic.log.extended.LoggingUtil
 * @version 1.0, 2012-12-19 上午11:18:37
 * @author quanyongan
 */
public class LoggingUtil {

	public static Logger log = LoggerFactory.getLogger(FrameworkConst.LOG_EXCEPTION);

	public static void info(String appCode, String accountId, String ip, String action, String message) {
		if (FrameworkUtil.isNullOrEmpty(appCode))
			appCode = "";
		if (FrameworkUtil.isNullOrEmpty(accountId))
			accountId = "";
		if (FrameworkUtil.isNullOrEmpty(ip))
			ip = "";
		if (FrameworkUtil.isNullOrEmpty(action))
			action = "";
		if (FrameworkUtil.isNullOrEmpty(message))
			message = "";
		MDC.put(LoggingEventConst.APP_CODE, appCode);
		MDC.put(LoggingEventConst.ACCOUNT_ID, accountId);
		MDC.put(LoggingEventConst.IP, ip);
		MDC.put(LoggingEventConst.ACTION, action);
		MDC.put(LoggingEventConst.CREATE_DATE, new Date(System.currentTimeMillis()));
		log.info(message);
	}
}
