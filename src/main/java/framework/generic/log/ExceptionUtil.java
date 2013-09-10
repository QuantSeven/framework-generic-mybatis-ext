package framework.generic.log;

import java.util.Date;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import framework.generic.util.FrameworkConst;

/**
 * 异常信息处理类
 * <p>
 * 项目名称：framework-generic
 * </p>
 * <p>
 * 版权：2012-广州扬基信息科技有限公司
 * </p>
 * 
 * @see framework.generic.mybatis.log.ExceptionUtil
 * @version 1.0, 2012-12-21 上午9:23:07
 * @author quanyongan
 */
public class ExceptionUtil {

	public static Logger loger = LoggerFactory.getLogger(FrameworkConst.LOG_EXCEPTION);

	/**
	 * 只提供error方法
	 * 
	 * @param appId
	 *            应用系统的ID
	 * @param t
	 *            错误信息
	 * @param clazz
	 *            报异常的类
	 */
	@SuppressWarnings("rawtypes")
	public static void error(String appCode, Throwable t, Class clazz) {
		String msg = doProcessData(appCode, t, clazz);
		loger.error("\n错误如下:" + t.toString() + "\n" + msg);
	}

	@SuppressWarnings("rawtypes")
	public static void error(String appCode, Exception ex, Class clazz, String message) {
		String msg = doProcessData(appCode, ex, clazz);
		loger.error(message + "\n### 错误如下:\n" + ex.toString() + "\n" + msg);
	}

	@SuppressWarnings("rawtypes")
	private static String doProcessData(String appCode, Throwable t, Class clazz) {
		String msg = "\n### 报错相关类、方法、行号:\n";
		for (StackTraceElement e : t.getStackTrace()) {
			if (clazz.getName().equals(e.getClassName())) {
				MDC.put(LoggingEventConst.APP_CODE, appCode);
				MDC.put(LoggingEventConst.CLASS_NAME, e.getClassName());
				MDC.put(LoggingEventConst.METHOD_NAME, e.getMethodName());
				MDC.put(LoggingEventConst.LINE_NUM, e.getLineNumber());
				MDC.put(LoggingEventConst.CREATE_DATE, new Date(System.currentTimeMillis()));
			}
			if (e.getClassName().contains("com.pousheng")) {
				msg += "at " + e.getClassName() + "." + e.getMethodName() + "(" + e.getFileName() + " : " + e.getLineNumber() + ")" + "\n";
			}
		}
		return msg;
	}
}
