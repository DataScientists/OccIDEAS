package org.occideas.vo;

import java.util.List;

public class IndividualFindingVO {

  private String agentName;
  private String text;
  // Only meaningful for "other findings" (probMedium/probLow) and assessed agent rows
  // (also probHigh/unknown/none) - used to pick the severity color in the PDF. Unused for high findings.
  private String level;
  // High findings only - each fired High rule's rationale for this agent ("Why this was estimated as High").
  private List<String> reasons;

  public List<String> getReasons() {
    return reasons;
  }

  public void setReasons(List<String> reasons) {
    this.reasons = reasons;
  }

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
