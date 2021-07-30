package org.occideas.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Interview_FiredRules")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
public class InterviewFiredRules implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "idInterview_FiredRules")
  private long id;
  @Column(name = "idinterview")
  private long idinterview;
  @Column(name = "idRule")
  private long idRule;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumns({
    @JoinColumn(name = "idRule", referencedColumnName = "idRule", insertable = false, updatable = false)})
  private List<Rule> rules;

  //@OneToMany(fetch = FetchType.LAZY)
  //@JoinColumns({
  //  @JoinColumn(name = "idinterview", referencedColumnName = "idinterview", insertable = false, updatable = false)})
  //private List<Interview> interviews;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getIdinterview() {
    return idinterview;
  }

  public void setIdinterview(long idinterview) {
    this.idinterview = idinterview;
  }

  public long getIdRule() {
    return idRule;
  }

  public void setIdRule(long idRule) {
    this.idRule = idRule;
  }

  public List<Rule> getRules() {
    return rules;
  }

  public void setRules(List<Rule> rules) {
    this.rules = rules;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    InterviewFiredRules that = (InterviewFiredRules) o;
    return idinterview == that.idinterview && idRule == that.idRule;
  }

  @Override
  public int hashCode() {
    return Objects.hash(idinterview, idRule);
  }

  /*
  public List<Interview> getInterviews() {
    return interviews;
  }

  public void setInterviews(List<Interview> interviews) {
    this.interviews = interviews;
  }
*/
}
