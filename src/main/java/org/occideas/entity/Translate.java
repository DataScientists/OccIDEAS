package org.occideas.entity;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

//@Entity
//@Table(name = "Translate")
//@DynamicUpdate(value = true)
//@DynamicInsert(value = true)
//@SelectBeforeUpdate(value = true)
public class Translate {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String language;
  private String description;
  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private Date lastUpdated;
  private String flag;
  private String jdoc;


  public long getId() {
    return id;
  }


  public void setId(long id) {
    this.id = id;
  }


  public String getLanguage() {
    return language;
  }


  public void setLanguage(String language) {
    this.language = language;
  }


  public String getDescription() {
    return description;
  }


  public void setDescription(String description) {
    this.description = description;
  }


  public Date getLastUpdated() {
    return lastUpdated;
  }


  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }


  public String getFlag() {
    return flag;
  }


  public void setFlag(String flag) {
    this.flag = flag;
  }


  public String getJdoc() {
    return jdoc;
  }


  public void setJdoc(String jdoc) {
    this.jdoc = jdoc;
  }

}
