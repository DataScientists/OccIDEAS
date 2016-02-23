package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class PossibleAnswerVO extends NodeVO {

	@JsonInclude(Include.NON_NULL)
	@JsonProperty(value = "nodes")
	private List<QuestionVO> childNodes;

	private QuestionVO parent;
		
	public List<QuestionVO> getChildNodes() {
		if(childNodes == null){
			childNodes = new ArrayList<QuestionVO>();
		}
		return childNodes;
	}

	public void setChildNodes(List<QuestionVO> childNodes) {
		this.childNodes = childNodes;
	}

	public QuestionVO getParent() {
		return parent;
	}

	public void setParent(QuestionVO parent) {
		this.parent = parent;
	}
	
}
