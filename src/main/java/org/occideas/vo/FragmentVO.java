package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FragmentVO extends NodeVO {

  @JsonInclude(Include.NON_NULL)
  @JsonProperty(value = "nodes")
  private List<QuestionVO> childNodes;

  public List<QuestionVO> getChildNodes() {
    if (childNodes == null) {
      childNodes = new ArrayList<QuestionVO>();
    }
    return childNodes;
  }

  public void setChildNodes(List<QuestionVO> childNodes) {
    this.childNodes = childNodes;
  }

  @Override
  public String getNodeType() {
    return "Fragment";
  }

}
