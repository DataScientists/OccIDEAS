package org.occideas.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Interview_FiredRules")
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

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "idRule", insertable=false, updatable=false, referencedColumnName = "idRule")
  private List<Rule> rules = new ArrayList<>();

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
