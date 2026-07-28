package org.occideas.vo;

import java.util.List;

public class EmailReportVO {

  private Long interviewId;
  private String email;
  private List<ReportAgentVO> agents;
  private List<ReportTreeNodeVO> tree;

  public Long getInterviewId() {
    return interviewId;
  }

  public void setInterviewId(Long interviewId) {
    this.interviewId = interviewId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<ReportAgentVO> getAgents() {
    return agents;
  }

  public void setAgents(List<ReportAgentVO> agents) {
    this.agents = agents;
  }

  public List<ReportTreeNodeVO> getTree() {
    return tree;
  }

  public void setTree(List<ReportTreeNodeVO> tree) {
    this.tree = tree;
  }
}
