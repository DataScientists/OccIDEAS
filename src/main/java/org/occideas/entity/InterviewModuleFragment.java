package org.occideas.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
@Table(name = "InterviewModule_Fragment")
public class InterviewModuleFragment implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  private String primaryKey;
  private long idFragment;
  private long interviewPrimaryKey;
  private String fragmentNodeName;
  private String interviewId;
  private String interviewFragmentName;

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

  public String getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(String primaryKey) {
    this.primaryKey = primaryKey;
  }

  public long getInterviewPrimaryKey() {
    return interviewPrimaryKey;
  }

  public void setInterviewPrimaryKey(long interviewPrimaryKey) {
    this.interviewPrimaryKey = interviewPrimaryKey;
  }

}
