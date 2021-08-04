package org.occideas.utilities;

import org.occideas.vo.PageVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class PageUtil<T> {

  public PageVO<T> populatePage(List<T> content, int pageNumber, int sizePerPage) {
    PageVO<T> page = new PageVO<>();
    page.setContent(content == null ? new ArrayList<>() : content);
    page.setHasContent(!content.isEmpty());
    page.setPageNumber(pageNumber);
    page.setSizePerPage(sizePerPage);
    page.setTotalPages(page.getTotalSize() / sizePerPage);
    return page;
  }

  public int calculatePageIndex(int pageNumber, int sizePerPage) {
    pageNumber = pageNumber == 1 ? 0 : pageNumber;
    if (pageNumber > 0) {
      pageNumber = pageNumber - 1;
      pageNumber = pageNumber * sizePerPage;
    }
    return pageNumber;
  }


}
