package org.occideas.vo;

import org.hibernate.SQLQuery;

public abstract class GenericFilterVO {

	public abstract void applyFilter(GenericFilterVO filter, final SQLQuery sqlQuery);
	
	protected Integer pageNumber;
	protected Integer size;
	
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
