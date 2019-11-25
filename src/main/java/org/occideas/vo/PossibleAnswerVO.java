package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.utilities.CommonUtil;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PossibleAnswerVO extends NodeVO implements Comparable<PossibleAnswerVO> {

  @JsonInclude(Include.NON_NULL)
  @JsonProperty(value = "nodes")
  private List<QuestionVO> childNodes;
  private QuestionVO parent;

  public PossibleAnswerVO() {
  }

  public PossibleAnswerVO(long idNode) {
    this.idNode = idNode;
  }

  public PossibleAnswerVO(String number, String name) {
    this.number = number;
    this.name = name;
  }

  public PossibleAnswerVO(List<QuestionVO> childNodes, QuestionVO parent) {
    this.childNodes = childNodes;
    this.parent = parent;
  }

  public List<QuestionVO> getChildNodes() {
    if (childNodes == null) {
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
    if (o == null) {
      return 1;
    } else {
      String nodeNumberA = o.getNumber();
      String nodeNumberB = this.getNumber();
      if ((CommonUtil.isNumeric(nodeNumberA)) && (CommonUtil.isNumeric(nodeNumberB))) {
        Integer iNodeNumberA = Integer.valueOf(nodeNumberA);
        Integer iNodeNumberB = Integer.valueOf(nodeNumberB);
        return iNodeNumberB.compareTo(iNodeNumberA);
      } else {
        return nodeNumberB.compareTo(nodeNumberA);
      }
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (int) (prime * result + (idNode));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PossibleAnswerVO other = (PossibleAnswerVO) obj;
    if (idNode == 0) {
      return other.idNode == 0;
    } else return idNode == (other.idNode);
  }

  @Override
  public String getNodeType() {
    return "Answer";
  }

}
