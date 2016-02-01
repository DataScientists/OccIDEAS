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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "node_discriminator", discriminatorType=DiscriminatorType.STRING)
@JsonRootName(value = "Nodes")
public class Node implements Cloneable {
	@Id
	private long idNode;
	
	@Column(length=2048)
	private String name;
	
	@Column(length=2048)
	private String description;
	
	private String type;
	private int sequence;
	private String number;
	
	@Column(name="parent_idNode")
	private String parentId;
	
	private long link;
	private long topNodeId;
	private Date lastUpdated;
	
	@OneToMany(mappedBy="parentId")
	@Cascade(value=CascadeType.SAVE_UPDATE)
	@Where(clause = "deleted = 0")
	@OrderBy("sequence ASC")
	private List<Node> childNodes;
	
	@OneToMany(mappedBy="node", fetch = FetchType.EAGER)
	@JsonInclude(Include.NON_EMPTY)
	private List<Note> notes;
	
	
	private long originalId;
	private Integer deleted = 0;
	private String nodeclass;

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
		this.childNodes = node.getChildNodes();
		
	}

	public void addChild(Node childNode) {
		childNode.setParentId(this.getParentId());
		this.setChildNodes(getChildNodes() == null ? new ArrayList<Node>() : getChildNodes());
		getChildNodes().add(childNode);
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

	public List<Node> getChildNodes() {
		return childNodes = childNodes == null ? new ArrayList<Node>() : childNodes;
	}

	public void setChildNodes(List<Node> childNodes) {
		this.childNodes = childNodes;
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
	

}