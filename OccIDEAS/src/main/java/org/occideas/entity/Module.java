package org.occideas.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Where;

@Entity
@Table(name="Node")
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
@SelectBeforeUpdate(value=true)
public class Module extends Node implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7963940691772676956L;
	
	@OneToMany(mappedBy="parentId",targetEntity=Node.class)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.PERSIST})
	@Where(clause = "deleted = 0")
	@OrderBy("sequence ASC")
	private List<Question> childNodes;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idModule",referencedColumnName="idNode",updatable=false)
	protected List<ModuleRule> moduleRule; 
	
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

	public List<Question> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<Question> childNodes) {
		this.childNodes = childNodes;
	}

	public List<ModuleRule> getModuleRule() {
		return moduleRule;
	}

	public void setModuleRule(List<ModuleRule> moduleRule) {
		this.moduleRule = moduleRule;
	}
	
	
}
