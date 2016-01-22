package org.occideas.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("M")
public class Module extends Node{

	@Transient
	private List<ModuleRule> moduleRules;
	
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

	public void addChild(Question childNode) {
		childNode.setParent(this);
		super.setChildNodes(getChildNodes() == null?new ArrayList<Node>():getChildNodes());
		getChildNodes().add(childNode);
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
}
