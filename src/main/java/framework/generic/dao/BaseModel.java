package framework.generic.dao;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class BaseModel
{
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }

  public boolean equals(Object paramObject)
  {
    return EqualsBuilder.reflectionEquals(this, paramObject);
  }

  public int hashCode()
  {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}