package org.occideas.vo;

public class ModuleCopyVO {

	private ModuleVO vo;
	private String name;
	private boolean includeRules;

	public ModuleVO getVo() {
		return vo;
	}

	public void setVo(ModuleVO vo) {
		this.vo = vo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIncludeRules() {
		return includeRules;
	}

	public void setIncludeRules(boolean includeRules) {
		this.includeRules = includeRules;
	}

}
