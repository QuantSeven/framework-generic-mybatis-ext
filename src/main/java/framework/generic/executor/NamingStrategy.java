package framework.generic.executor;

import java.lang.reflect.Method;

public abstract interface NamingStrategy {
	public abstract String getNameFor(Class<?> paramClass, Method paramMethod);
}