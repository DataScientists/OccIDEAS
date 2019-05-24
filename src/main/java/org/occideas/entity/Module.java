package org.occideas.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("M")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
public class Module extends Node<Question> implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -7963940691772676956L;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "idModule", referencedColumnName = "idNode", updatable = false)
  protected List<ModuleRule> moduleRule;


  public Module() {
    super();
  }

  public Module(Node node) {
    super(node);
  }


  public Module(long idNode) {
    super();
    this.setIdNode(idNode);
  }


  public Module(String idNode) {
    super();
    this.setIdNode(Long.parseLong(idNode));
  }


  public void addNote(Note note) {
    note.setNode(this);
    this.setNotes(this.getNotes() == null ? new ArrayList<Note>() : this.getNotes());
    this.getNotes().add(note);
  }


  public List<ModuleRule> getModuleRule() {
    return moduleRule;
  }


  public void setModuleRule(List<ModuleRule> moduleRule) {
    this.moduleRule = moduleRule;
  }

}
