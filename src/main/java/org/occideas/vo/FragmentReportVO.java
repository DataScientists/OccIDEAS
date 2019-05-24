package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

public class FragmentReportVO extends BaseReportVO {

  private FragmentVO vo;
  private List<String> issues;

  public FragmentVO getVo() {
    return vo;
  }

  public void setVo(FragmentVO vo) {
    this.vo = vo;
  }

  public List<String> getIssues() {
    return issues;
  }

  public void setIssues(List<String> issues) {
    this.issues = issues;
  }

  public void addIssue(String issue) {
    if (issues == null) {
      issues = new ArrayList();
    }
    issues.add(issue);
  }


}
