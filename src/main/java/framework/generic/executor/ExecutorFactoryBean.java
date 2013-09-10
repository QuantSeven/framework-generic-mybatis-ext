package framework.generic.executor;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class ExecutorFactoryBean implements FactoryBean<Object>, InitializingBean {
	private Class<?> mapperInterface;
	private Executor executor;

	public Class<?> getMapperInterface() {
		return mapperInterface;
	}

	public void setMapperInterface(Class<?> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	public void setExecutor(Executor paramExecutor) {
		this.executor = paramExecutor;
	}

	public Executor getExecutor() {
		return this.executor;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.mapperInterface, "Property 'mapperInterface' is required");
		Assert.notNull(this.executor, "Property 'executor' is required");
	}

	public Object getObject() throws Exception {
		return ExecutorProxy.newExecutorProxy(this.mapperInterface, this.executor);
	}

	public Class<?> getObjectType() {
		return this.mapperInterface;
	}

	public boolean isSingleton() {
		return true;
	}
}