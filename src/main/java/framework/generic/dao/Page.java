package framework.generic.dao;

import java.util.List;

public class Page<T>
{
  private int a = 1;
  private int b = -1;
  private long c = -1L;
  private List<T> d = null;
  private boolean e = false;

  public Page()
  {
  }

  public Page(int paramInt)
  {
    this.b = paramInt;
  }

  public int getNumber()
  {
    return this.a;
  }

  public void setNumber(int paramInt)
  {
    this.a = paramInt;
    if (paramInt < 1)
      this.a = 1;
  }

  public Page<T> number(int paramInt)
  {
    setNumber(paramInt);
    return this;
  }

  public int getSize()
  {
    return this.b;
  }

  public void setSize(int paramInt)
  {
    this.b = paramInt;
  }

  public Page<T> size(int paramInt)
  {
    setSize(paramInt);
    return this;
  }

  public int getFirst()
  {
    return getOneBasedFirst();
  }

  public int getZeroBasedFirst()
  {
    return (this.a - 1) * this.b;
  }

  public int getOneBasedFirst()
  {
    return getZeroBasedFirst() + 1;
  }

  public int getLast()
  {
    return getOneBasedFirstPage();
  }

  public int getZeroBasedFirstPage()
  {
    return (this.a - 1) * this.b + this.b;
  }

  public int getOneBasedFirstPage()
  {
    return getZeroBasedFirstPage() + 1;
  }

  public boolean isAutoRecords()
  {
    return this.e;
  }

  public void setAutoRecords(boolean paramBoolean)
  {
    this.e = paramBoolean;
  }

  public Page<T> autoRecordCount(boolean paramBoolean)
  {
    setAutoRecords(paramBoolean);
    return this;
  }

  public List<T> getRecords()
  {
    return this.d;
  }

  public void setRecords(List<T> paramList)
  {
    this.d = paramList;
  }

  public long getTotalRecords()
  {
    return this.c;
  }

  public void setTotalRecords(long paramLong)
  {
    this.c = paramLong;
    this.e = false;
  }

  public long getCount()
  {
    if (this.c < 0L)
      return -1L;
    long l = this.c / this.b;
    if (this.c % this.b > 0L)
      l += 1L;
    return l;
  }

  public boolean hasNext()
  {
    return this.a + 1 <= getCount();
  }

  public int getNext()
  {
    if (hasNext())
      return this.a + 1;
    return this.a;
  }

  public boolean hasPrevious()
  {
    return this.a - 1 >= 1;
  }

  public int getPrevious()
  {
    if (hasPrevious())
      return this.a - 1;
    return this.a;
  }
}