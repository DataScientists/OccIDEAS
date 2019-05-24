package org.occideas.vo;

import java.util.List;

public class PageVO<T> {

  private List<T> content;
  private int pageSize;
  private int totalSize;
  private int totalPages;
  private int pageNumber;
  private int sizePerPage;
  private int calculatedPageNumber;
  private boolean hasContent;
  private GenericFilterVO filterVO;

  public List<T> getContent() {
    return content;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getTotalSize() {
    return totalSize;
  }

  public void setTotalSize(int totalSize) {
    this.totalSize = totalSize;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public int getSizePerPage() {
    return sizePerPage;
  }

  public void setSizePerPage(int sizePerPage) {
    this.sizePerPage = sizePerPage;
  }

  public boolean isHasContent() {
    return hasContent;
  }

  public void setHasContent(boolean hasContent) {
    this.hasContent = hasContent;
  }

  public int getCalculatedPageNumber() {
    return calculatedPageNumber;
  }

  public void setCalculatedPageNumber(int pageNumber, int sizePerPage) {
    pageNumber = pageNumber == 1 ? 0 : pageNumber;
    if (pageNumber > 0) {
      pageNumber = pageNumber - 1;
      pageNumber = pageNumber * sizePerPage;
    }
    this.calculatedPageNumber = pageNumber;
  }

  public GenericFilterVO getFilterVO() {
    return filterVO;
  }

  public void setFilterVO(GenericFilterVO filterVO) {
    this.filterVO = filterVO;
  }


}
