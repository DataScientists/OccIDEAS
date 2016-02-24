package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"editEnabled","info","warning","placeholder","isEditing","showAgentSlider","id","collapsed"})
public class QuestionVO extends NodeVO{

	@JsonInclude(Include.NON_NULL)
	@JsonProperty(value = "nodes")
	private List<PossibleAnswerVO> childNodes;
	
	private PossibleAnswerVO parent;

	
	public List<PossibleAnswerVO> getChildNodes() {
		if(childNodes == null){
			childNodes = new ArrayList<PossibleAnswerVO>();
		}
		return childNodes;
	}

	public void setChildNodes(List<PossibleAnswerVO> childNodes) {
		this.childNodes = childNodes;
	}

	public PossibleAnswerVO getParent() {
		return parent;
	}

	public void setParent(PossibleAnswerVO parent) {
		this.parent = parent;
	}
	
}
