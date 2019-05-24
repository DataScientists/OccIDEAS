package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterModuleVO {

	private String fileName;
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

}
