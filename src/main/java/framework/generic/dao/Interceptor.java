package framework.generic.dao;

public abstract interface Interceptor
{
  public static final String CREATE = "CREATE";
  public static final String UPDATE = "UPDATE";
  public static final String DELETE = "DELETE";

  public abstract void before(String paramString, Object paramObject1, Object paramObject2);

  public abstract void after(String paramString, Object paramObject1, Object paramObject2);

  public abstract boolean supports(Class<?> paramClass);

  public abstract boolean supports(Object paramObject);
}