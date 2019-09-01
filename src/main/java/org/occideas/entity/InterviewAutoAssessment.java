package org.occideas.entity;

import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;

@Entity
@Table(name = "Interview_AutoAssessedRules")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
public class InterviewAutoAssessment implements java.io.Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "idInterview_AutoAssessedRules")
  private long id;
  @Column(name = "idinterview")
  private long idInterview;
  @OneToOne(fetch = FetchType.LAZY)
  @Cascade(value = {CascadeType.SAVE_UPDATE, CascadeType.PERSIST})
  @JoinColumn(name = "idRule", referencedColumnName = "idRule", insertable = true, updatable = true)
  private Rule rule;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getIdInterview() {
    return idInterview;
  }

  public void setIdInterview(long idInterview) {
    this.idInterview = idInterview;
  }

  public Rule getRule() {
    return rule;
  }

  public void setRule(Rule rule) {
    this.rule = rule;
  }

}
