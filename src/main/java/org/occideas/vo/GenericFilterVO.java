package org.occideas.vo;

import org.hibernate.SQLQuery;

public abstract class GenericFilterVO {

  protected Integer pageNumber;
  protected Integer size;

  public abstract void applyFilter(GenericFilterVO filter, final SQLQuery sqlQuery);

  public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }
}
