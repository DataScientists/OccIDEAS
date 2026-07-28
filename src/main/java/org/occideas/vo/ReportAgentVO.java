package org.occideas.vo;

import java.util.List;

public class ReportAgentVO {

  private String name;
  private List<ReportRuleVO> rules;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<ReportRuleVO> getRules() {
    return rules;
  }

  public void setRules(List<ReportRuleVO> rules) {
    this.rules = rules;
  }
}
