package org.occideas.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;

@Entity
@DiscriminatorValue("M")
public class Module extends Node implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7963940691772676956L;
	@Transient
	private List<ModuleRule> moduleRules;
	
	@OneToMany(mappedBy="parentId",targetEntity=Node.class)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.PERSIST})
	@Where(clause = "deleted = 0")
	@OrderBy("sequence ASC")
	private List<Question> childNodes;
	
	public Module() {
		super();
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
		this.setNotes(this.getNotes() == null?new ArrayList<Note>():this.getNotes());
		this.getNotes().add(note);
	}

	public List<ModuleRule> getModuleRules() {
		return moduleRules;
	}

	public void setModuleRules(List<ModuleRule> moduleRules) {
		this.moduleRules = moduleRules;
	}

	public List<Question> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<Question> childNodes) {
		this.childNodes = childNodes;
	}
	
	
}
