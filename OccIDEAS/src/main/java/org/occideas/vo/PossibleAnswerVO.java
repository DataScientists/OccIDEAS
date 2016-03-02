package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

import org.occideas.utilities.CommonUtil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties({"isOpen","selectedAnswer","editEnabled","info","warning","placeholder","isEditing","showAgentSlider","id","collapsed"})
public class PossibleAnswerVO extends NodeVO implements Comparable<PossibleAnswerVO>{

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
	@Override
	public int compareTo(PossibleAnswerVO o) {
		if(o==null){
			return 1;
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
