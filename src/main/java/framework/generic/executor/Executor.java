package framework.generic.executor;

import java.lang.reflect.Method;

public abstract interface Executor
{
  public abstract boolean supports(Method paramMethod);

  public abstract Object execute(Class<?> paramClass, Method paramMethod, Object[] paramArrayOfObject);
}