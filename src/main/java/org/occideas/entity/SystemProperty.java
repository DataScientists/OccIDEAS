package org.occideas.entity;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SYS_CONFIG")
public class SystemProperty implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @Column(name = "type")
  private String type;
  @Column(name = "name")
  private String name;
  @Column(name = "value")
  private String value;
  @Column(name = "updatedDt")
  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private Date updatedDt;
  @Column(name = "updatedBy")
  private String updatedBy;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Date getUpdatedDt() {
    return updatedDt;
  }

  public void setUpdatedDt(Date updatedDt) {
    this.updatedDt = updatedDt;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

}
