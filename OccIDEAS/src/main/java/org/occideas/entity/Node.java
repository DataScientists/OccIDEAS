package org.occideas.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "node_discriminator", discriminatorType=DiscriminatorType.STRING)
@JsonRootName(value = "Nodes")
public class Node implements Cloneable {
	@Id
	protected long idNode;
	
	@Column(length=2048)
	protected String name;
	
	@Column(length=2048)
	protected String description;
	
	protected String type;
	protected int sequence;
	protected String number;
	
	@Column(name="parent_idNode")
	protected String parentId;
	
	protected long link;
	protected long topNodeId;
	protected Date lastUpdated;
	
	@OneToMany(mappedBy="node", fetch = FetchType.EAGER)
	@JsonInclude(Include.NON_EMPTY)
	protected List<Note> notes;
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idNode",referencedColumnName="idNode")
	protected List<ModuleRule> moduleRule; 
	
	protected long originalId;
	protected Integer deleted = 0;
	protected String nodeclass;

	public Node() {
		super();
	}

	public Node(Node node) {
		this.name = node.getName();
		this.description = node.getDescription();
		this.type = node.getType();
		this.sequence = node.getSequence();
		this.number = node.getNumber();
		this.parentId = node.getParentId();
		
		this.link = node.getLink();
		this.topNodeId = node.getTopNodeId();
		this.lastUpdated = node.getLastUpdated();
		this.originalId = node.getOriginalId();
		this.deleted = node.getDeleted();
		
	}

	public void addNote(Note note) {
		note.setNode(this);
		this.setNotes(this.getNotes() == null?new ArrayList<Note>():this.getNotes());
		this.getNotes().add(note);
	}

	public Node(String idNode) {
		super();
		this.setIdNode(Long.parseLong(idNode));
	}

	public Node(long idNode) {
		super();
		this.setIdNode(idNode);
	}

	public long getIdNode() {
		return idNode;
	}

	public void setIdNode(long idNode) {
		this.idNode = idNode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		/*if (type == null) {
			type = "Module";
		} else if (type.equalsIgnoreCase("")) {
			type = "Module";
		}*/
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public long getLink() {
		return link;
	}

	public void setLink(long link) {
		this.link = link;
	}

	public long getTopNodeId() {
		return topNodeId;
	}

	public void setTopNodeId(long topNodeId) {
		this.topNodeId = topNodeId;
	}

	public long getOriginalId() {
		return originalId;
	}

	public void setOriginalId(long originalId) {
		this.originalId = originalId;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getNodeclass() {
		return nodeclass;
	}

	public void setNodeclass(String nodeclass) {
		this.nodeclass = nodeclass;
	}
	
	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public List<ModuleRule> getModuleRule() {
		return moduleRule;
	}

	public void setModuleRule(List<ModuleRule> moduleRule) {
		this.moduleRule = moduleRule;
	}

}