package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"selectedAnswer","editEnabled","info","warning","placeholder","isEditing","showAgentSlider","id","collapsed"})
public class QuestionVO extends NodeVO implements Cloneable{

	@JsonInclude(Include.NON_NULL)
	@JsonProperty(value = "nodes")
	private List<PossibleAnswerVO> childNodes;
	
	private PossibleAnswerVO parent;
	
	private QuestionVO linkingQuestion;
	
	private Long activeInterviewId;

	
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

	public QuestionVO getLinkingQuestion() {
		return linkingQuestion;
	}

	public void setLinkingQuestion(QuestionVO linkingQuestion) {
		this.linkingQuestion = linkingQuestion;
	}

	@Override public QuestionVO clone() {
        try {
            final QuestionVO result = (QuestionVO) super.clone();
            // copy fields that need to be copied here!
            return result;
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError();
        }
	}

	public Long getActiveInterviewId() {
		return activeInterviewId;
	}

	public void setActiveInterviewId(Long activeInterviewId) {
		this.activeInterviewId = activeInterviewId;
	}	
}
