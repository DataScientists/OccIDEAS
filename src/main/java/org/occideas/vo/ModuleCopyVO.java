package org.occideas.vo;

import java.util.List;

public class ModuleCopyVO {

	private ModuleVO vo;
	private String name;
	private boolean includeRules;
	private boolean includeLinks;
	private List<FragmentVO> fragments;
	private List<ModuleVO> modules;

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

	public boolean isIncludeLinks() {
		return includeLinks;
	}

	public void setIncludeLinks(boolean includeLinks) {
		this.includeLinks = includeLinks;
	}

	public List<FragmentVO> getFragments() {
		return fragments;
	}

	public void setFragments(List<FragmentVO> fragments) {
		this.fragments = fragments;
	}

	public List<ModuleVO> getModules() {
		return modules;
	}

	public void setModules(List<ModuleVO> modules) {
		this.modules = modules;
	}

}