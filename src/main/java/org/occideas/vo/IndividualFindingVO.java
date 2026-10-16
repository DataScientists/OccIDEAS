package org.occideas.vo;

public class IndividualFindingVO {

  private String agentName;
  private String text;
  // Only meaningful for "other findings" (probMedium/probLow) and assessed agent rows
  // (also probHigh/unknown/none) - used to pick the severity color in the PDF. Unused for high findings.
  private String level;

  public String getAgentName() {
    return agentName;
  }

  public void setAgentName(String agentName) {
    this.agentName = agentName;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }
}
