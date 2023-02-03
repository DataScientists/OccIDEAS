package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterModuleVO {

  private String fileName;
  private Boolean sortColumns;
  private String[] filterModule;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String[] getFilterModule() {
    return filterModule;
  }

  public void setFilterModule(String[] filterModule) {
    this.filterModule = filterModule;
  }

  public Boolean getSortColumns() {
	if(sortColumns==null) {
	  sortColumns = false;
	}
	return sortColumns;
  }

  public void setSortColumns(Boolean sortColumns) {
	this.sortColumns = sortColumns;
  }

}
