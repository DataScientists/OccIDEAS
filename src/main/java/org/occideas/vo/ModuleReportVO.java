package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

public class ModuleReportVO extends BaseReportVO{

	private ModuleVO vo;
	private List<String> issues;

	public ModuleVO getVo() {
		return vo;
	}

	public List<String> getIssues() {
		return issues;
	}

	public void setIssues(List<String> issues) {
		this.issues = issues;
	}

	public void setVo(ModuleVO vo) {
		this.vo = vo;
	}

	public void addIssue(String issue) {
		if(issues == null){
			issues = new ArrayList();
		}
		issues.add(issue);
	}

}
