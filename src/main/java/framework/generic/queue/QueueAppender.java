package framework.generic.queue;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import framework.generic.log.LoggingEventWrapper;

/**
 * 轻量级的Log4j异步Appender. 将所有消息放入QueueManager所管理的Blocking Queue中.
 * <p>
 * framework-generic-mybatis
 * </p>
 * <p>
 * 项目名称：2013-广州扬基信息科技有限公司
 * </p>
 * 
 * @see framework.generic.mybatis.log.QueueAppender
 * @version 1.0, 2013-8-28 下午2:17:03
 * @author quanyongan
 */
public class QueueAppender extends AppenderSkeleton {

	protected String queueName;

	protected BlockingQueue<LoggingEvent> queue;
	protected LoggingEvent loggingEvent;

	/**
	 * AppenderSkeleton回调函数, 事件到达时将时间放入Queue.
	 */
	@Override
	public void append(LoggingEvent event) {
		if (queue == null) {
			queue = QueuesHolder.getQueue(queueName);
		}
		boolean sucess = queue.offer(event);
		if (sucess) {
			LogLog.debug("put event to queue success:" + new LoggingEventWrapper(event).convertToString() + event.getProperties());

		} else {
			LogLog.error("Put event to queue fail:" + new LoggingEventWrapper(event).convertToString() + event.getProperties());
		}
	}

	/**
	 * AppenderSkeleton回调函数,关闭Logger时的清理动作.
	 */
	public void close() {
	}

	/**
	 * AppenderSkeleton回调函数, 设置是否需要定义Layout.
	 */
	public boolean requiresLayout() {
		return false;
	}

	/**
	 * Log4j根据getter/setter从log4j.properties中注入同名参数.
	 */
	public String getQueueName() {
		return queueName;
	}

	/**
	 * @see #getQueueName()
	 */
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
}
