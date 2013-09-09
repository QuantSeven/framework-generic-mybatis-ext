package framework.generic.dao;

import java.io.Serializable;
import java.util.List;

public abstract interface GenericDao<T extends Model<PK>, PK extends Serializable>
{
  public static final String EXECUTE_CREATE = "create";
  public static final String EXECUTE_READ = "read";
  public static final String EXECUTE_UPDATE = "update";
  public static final String EXECUTE_DELETE = "delete";
  public static final String EXECUTE_COUNT = "count";
  public static final String EXECUTE_FIND = "find";
  public static final String PARAMETER_PAGE = "page";
  public static final String PARAMETER_ORDERS = "orders";
  public static final String PARAMETER_ARGS = "args";

  public abstract T create(T paramT);

  public abstract T read(PK paramPK);

  public abstract List<T> read();

  public abstract T update(T paramT);

  public abstract void delete(T paramT);
}