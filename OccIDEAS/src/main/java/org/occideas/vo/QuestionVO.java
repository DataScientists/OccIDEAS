package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

import org.occideas.utilities.CommonUtil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"hours","minutes","isOpen","selectedAnswer","editEnabled","info","warning","placeholder","isEditing","showAgentSlider","id","collapsed"})
public class QuestionVO extends NodeVO implements Cloneable, Comparable<QuestionVO>{

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

	@Override 
	public QuestionVO clone() {
        try {
            final QuestionVO result = (QuestionVO) super.clone();
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

	@Override
	public int compareTo(QuestionVO o) {
		if(o==null){
			return -1; 
		}else{
			String nodeNumberA=o.getNumber();
			String nodeNumberB=this.getNumber();
			if( (CommonUtil.isNumeric(nodeNumberA)) && (CommonUtil.isNumeric(nodeNumberB)) ){
				Integer iNodeNumberA = Integer.valueOf(nodeNumberA);
				Integer iNodeNumberB = Integer.valueOf(nodeNumberB);			
				return iNodeNumberB.compareTo(iNodeNumberA);			
			}else{
				return nodeNumberB.compareTo(nodeNumberA);
			} 
		}	
	}	
}
