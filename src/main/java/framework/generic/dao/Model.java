package framework.generic.dao;

import java.io.Serializable;

public abstract interface Model<PK extends Serializable> extends Serializable
{
  public abstract PK getKey();
}