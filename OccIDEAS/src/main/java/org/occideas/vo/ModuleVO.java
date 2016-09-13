package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModuleVO extends NodeVO {

	@JsonInclude(Include.NON_NULL)
	@JsonProperty(value = "nodes")
	private List<QuestionVO> childNodes;

	private List<FragmentVO> fragments = new ArrayList<>();

	private List<ModuleVO> modules = new ArrayList<>();

	public List<QuestionVO> getChildNodes() {
		if (childNodes == null) {
			childNodes = new ArrayList<QuestionVO>();
		}
		return childNodes;
	}

	public void setChildNodes(List<QuestionVO> childNodes) {
		this.childNodes = childNodes;
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
