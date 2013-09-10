package framework.generic.queue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import framework.generic.util.FrameworkUtil;

/**
 * 单线程消费Queue中消息的任务基类.
 * 
 * 定义了QueueConsumer的启动关闭流程.
 * 
 * 支持多线程执行.
 * <p>
 * framework-generic-mybatis
 * </p>
 * <p>
 * 项目名称：2013-广州扬基信息科技有限公司
 * </p>
 * 
 * @see framework.generic.mybatis.queue.QueueConsumer
 * @version 1.0, 2013-8-29 上午10:33:27
 * @author quanyongan
 */
@SuppressWarnings("unchecked")
public abstract class QueueConsumer implements Runnable {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected String queueName;
	protected int shutdownTimeout = Integer.MAX_VALUE;

	protected boolean persistence = true;
	protected String persistencePath = System.getProperty("java.io.tmpdir") + File.separator + "queue";
	protected Object persistenceLock = new Object(); // 用于在backup与restore间等待的锁.

	protected BlockingQueue<LoggingEvent> queue;
	protected ExecutorService executor;

	/**
	 * 任务所消费的队列名称.
	 */
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	/**
	 * 停止任务时最多等待的时间, 单位为毫秒.
	 */
	public void setShutdownTimeout(int shutdownTimeout) {
		this.shutdownTimeout = shutdownTimeout;
	}

	/**
	 * 在JVM关闭时是否需要持久化未完成的消息到文件.
	 */
	public void setPersistence(boolean persistence) {
		this.persistence = persistence;
	}

	/**
	 * 系统关闭时将队列中未处理的消息持久化到文件的目录,默认为系统临时文件夹下的queue目录.
	 */
	public void setPersistencePath(String persistencePath) {
		this.persistencePath = persistencePath;
	}

	/**
	 * 任务初始化函数.
	 */
	@PostConstruct
	public void start() throws IOException, ClassNotFoundException, InterruptedException {
		queue = QueuesHolder.getQueue(queueName);
		executor = Executors.newSingleThreadExecutor(new FrameworkUtil.CustomizableThreadFactory("Queue Consumer-" + queueName));
		executor.execute(this);
	}

	/**
	 * 任务结束函数.
	 */
	@PreDestroy
	public void stop() throws IOException {
		try {
			FrameworkUtil.normalShutdown(executor, shutdownTimeout, TimeUnit.MILLISECONDS);
		} finally {
			if (persistence) {
				synchronized (persistenceLock) {
					backupQueue();
				}
			}
		}
	}

	/**
	 * 保存队列中的消息到文件. 目前不需要序列化到文件
	 */
	@SuppressWarnings("rawtypes")
	protected void backupQueue() throws IOException {
		List list = new ArrayList();
		queue.drainTo(list);
	}

	/**
	 * 消息处理函数.
	 */
	protected abstract void processMessage(Object message);

	/**
	 * 退出清理函数.
	 */
	protected abstract void clean();
}
