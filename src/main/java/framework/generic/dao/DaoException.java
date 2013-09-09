package framework.generic.dao;

public class DaoException extends RuntimeException
{
  public DaoException()
  {
  }

  public DaoException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }

  public DaoException(String paramString)
  {
    super(paramString);
  }

  public DaoException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}