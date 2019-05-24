package org.occideas.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InterviewRuleReport {

  @Column(name = "name")
  @CsvBindByName(column = "agentName", required = true)
  @CsvBindByPosition(position = 0)
  private String agentName;
  @Id
  @Column(name = "id")
  @CsvBindByName(column = "OccIDEASID", required = true)
  @CsvBindByPosition(position = 1)
  private String id;
  @Column(name = "idinterview")
  @CsvBindByName(column = "InterviewID", required = true)
  @CsvBindByPosition(position = 2)
  private long idInterview;
  @Column(name = "idRule")
  @CsvBindByName(column = "idRule", required = true)
  @CsvBindByPosition(position = 3)
  private long idRule;
  @Column(name = "level")
  @CsvBindByName(column = "level", required = true)
  @CsvBindByPosition(position = 4)
  private String level;
  @Column(name = "modName")
  @CsvBindByName(column = "modName", required = true)
  @CsvBindByPosition(position = 5)
  private String modName;
  @Column(name = "referenceNumber")
  @CsvBindByName(column = "ParticipantID", required = true)
  @CsvBindByPosition(position = 6)
  private String referenceNumber;

  public long getIdInterview() {
    return idInterview;
  }

  public void setIdInterview(long idInterview) {
    this.idInterview = idInterview;
  }

  public String getReferenceNumber() {
    return referenceNumber;
  }

  public void setReferenceNumber(String referenceNumber) {
    this.referenceNumber = referenceNumber;
  }

  public long getIdRule() {
    return idRule;
  }

  public void setIdRule(long idRule) {
    this.idRule = idRule;
  }

  public String getAgentName() {
    return agentName;
  }

  public void setAgentName(String agentName) {
    this.agentName = agentName;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getModName() {
    return modName;
  }

  public void setModName(String modName) {
    this.modName = modName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

}
