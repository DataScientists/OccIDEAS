package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LanguageJSONVO {

  List<NodeLanguageVO> vo;
  private String idNode;
  private String languageId;

  public String getIdNode() {
    return idNode;
  }


  public void setIdNode(String idNode) {
    this.idNode = idNode;
  }


  public List<NodeLanguageVO> getVo() {
    return vo;
  }


  public void setVo(List<NodeLanguageVO> vo) {
    this.vo = vo;
  }


  public String getLanguageId() {
    return languageId;
  }


  public void setLanguageId(String languageId) {
    this.languageId = languageId;
  }

}
