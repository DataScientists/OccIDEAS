package org.occideas.vo;

import java.util.List;

public class ReportRuleVO {

  private String level;
  private List<ReportConditionVO> conditions;

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public List<ReportConditionVO> getConditions() {
    return conditions;
  }

  public void setConditions(List<ReportConditionVO> conditions) {
    this.conditions = conditions;
  }
}
