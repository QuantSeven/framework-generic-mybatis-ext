package framework.generic.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.ognl.Ognl;

public class Parameters
  implements Map<String, Object>
{
  private Map<String, Object> a = new HashMap();

  public boolean containsKey(Object paramObject)
  {
    if (this.a.containsKey(paramObject))
      return true;
    try
    {
      if (Ognl.getValue(paramObject.toString(), this.a) != null)
        return true;
    }
    catch (Exception localException)
    {
    }
    return false;
  }

  public Object get(Object paramObject)
  {
    Object localObject;
    if (((localObject = this.a.get(paramObject)) == null) && (paramObject != null))
      try
      {
        localObject = Ognl.getValue(paramObject.toString(), this.a);
      }
      catch (Exception localException)
      {
      }
    return localObject;
  }

  public void clear()
  {
    this.a.clear();
  }

  public boolean containsValue(Object paramObject)
  {
    return this.a.containsValue(paramObject);
  }

  public Set<Map.Entry<String, Object>> entrySet()
  {
    return this.a.entrySet();
  }

  public boolean equals(Object paramObject)
  {
    return this.a.equals(paramObject);
  }

  public int hashCode()
  {
    return this.a.hashCode();
  }

  public boolean isEmpty()
  {
    return this.a.isEmpty();
  }

  public Set<String> keySet()
  {
    return this.a.keySet();
  }

  public Object put(String paramString, Object paramObject)
  {
    return this.a.put(paramString, paramObject);
  }

  public void putAll(Map<? extends String, ? extends Object> paramMap)
  {
    this.a.putAll(paramMap);
  }

  public Object remove(Object paramObject)
  {
    return this.a.remove(paramObject);
  }

  public int size()
  {
    return this.a.size();
  }

  public Collection<Object> values()
  {
    return this.a.values();
  }
}