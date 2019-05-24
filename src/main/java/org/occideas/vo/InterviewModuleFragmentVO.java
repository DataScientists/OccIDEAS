package org.occideas.vo;

public class InterviewModuleFragmentVO {

  private String primaryKey;
  private long idFragment;
  private long interviewPrimaryKey;
  private String fragmentNodeName;
  private String interviewId;
  private String interviewFragmentName;

  public String getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(String primaryKey) {
    this.primaryKey = primaryKey;
  }

  public long getIdFragment() {
    return idFragment;
  }

  public void setIdFragment(long idFragment) {
    this.idFragment = idFragment;
  }

  public String getFragmentNodeName() {
    return fragmentNodeName;
  }

  public void setFragmentNodeName(String fragmentNodeName) {
    this.fragmentNodeName = fragmentNodeName;
  }

  public String getInterviewId() {
    return interviewId;
  }

  public void setInterviewId(String interviewId) {
    this.interviewId = interviewId;
  }

  public String getInterviewFragmentName() {
    return interviewFragmentName;
  }

  public void setInterviewFragmentName(String interviewFragmentName) {
    this.interviewFragmentName = interviewFragmentName;
  }

  public long getInterviewPrimaryKey() {
    return interviewPrimaryKey;
  }

  public void setInterviewPrimaryKey(long interviewPrimaryKey) {
    this.interviewPrimaryKey = interviewPrimaryKey;
  }

}
