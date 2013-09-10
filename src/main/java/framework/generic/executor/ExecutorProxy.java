package framework.generic.executor;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ExecutorProxy implements Serializable, InvocationHandler {
	private static final long serialVersionUID = 3099053348039577939L;
	private Class<?> target;
	private Executor executor;

	private ExecutorProxy(Class<?> paramClass, Executor paramExecutor) {
		this.target = paramClass;
		this.executor = paramExecutor;
	}

	public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable {
		if (this.executor.supports(paramMethod))
			return this.executor.execute(this.target, paramMethod, paramArrayOfObject);
		return null;
	}
	

	public static Object newExecutorProxy(Class<?> paramClass, Executor paramExecutor) {
		ClassLoader localClassLoader = paramClass.getClassLoader();
		Class<?>[] arrayOfClass = { paramClass };
		InvocationHandler handler = new ExecutorProxy(paramClass, paramExecutor);
		return Proxy.newProxyInstance(localClassLoader, arrayOfClass, handler);
	}
}