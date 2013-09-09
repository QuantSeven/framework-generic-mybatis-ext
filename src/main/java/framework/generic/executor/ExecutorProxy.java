package framework.generic.executor;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ExecutorProxy implements Serializable, InvocationHandler {
	private Class<?> a;
	private Executor b;

	private ExecutorProxy(Class<?> paramClass, Executor paramExecutor) {
		this.a = paramClass;
		this.b = paramExecutor;
	}

	public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable {
		if (this.b.supports(paramMethod))
			return this.b.execute(this.a, paramMethod, paramArrayOfObject);
		return null;
	}

	public static Object newExecutorProxy(Class<?> paramClass, Executor paramExecutor) {
		ClassLoader localClassLoader = paramClass.getClassLoader();
		Class[] arrayOfClass = { paramClass };
		InvocationHandler handler = new ExecutorProxy(paramClass, paramExecutor);
		return Proxy.newProxyInstance(localClassLoader, arrayOfClass, handler);
	}
}