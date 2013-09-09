package framework.generic.dao;

public class Order
{
  private boolean a;
  private String b;

  public Order()
  {
  }

  public Order(String paramString)
  {
    this.b = paramString;
  }

  public Order(String paramString, boolean paramBoolean)
  {
    this.b = paramString;
    this.a = paramBoolean;
  }

  public static Order asc(String paramString)
  {
    return new Order(paramString, true);
  }

  public static Order desc(String paramString)
  {
    return new Order(paramString, false);
  }

  public boolean isAscending()
  {
    return this.a;
  }

  public void setAscending(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }

  public String getProperty()
  {
    return this.b;
  }

  public void setProperty(String paramString)
  {
    this.b = paramString;
  }

  public String toString()
  {
    return this.b + ' ' + (this.a ? "asc" : "desc");
  }
}