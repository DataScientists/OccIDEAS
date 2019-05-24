package org.occideas.vo;

public class FragmentCopyVO {

	private FragmentVO vo;
	private String name;
	private boolean includeRules;
	private boolean includeLinks;

	public FragmentVO getVo() {
		return vo;
	}

	public void setVo(FragmentVO vo) {
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

}
