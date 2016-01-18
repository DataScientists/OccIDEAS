package org.occideas.vo;

import java.util.Date;
import java.util.List;

import org.occideas.Node;
import org.occideas.Note;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ModuleVO {

	private long idNode;
	private String name;
	private String description;
	private String type;
	private int sequence;
	private String number;
	@JsonInclude(Include.NON_NULL)
	private Node parent;
	private long link;
	private long topNodeId;
	private Date lastUpdated;
	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty(value = "nodes")
	private List<Node> childNodes;
	@JsonInclude(Include.NON_EMPTY)
	private List<Note> notes;
	private long originalId;
	private Integer deleted = 0;
	private String nodeclass;

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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
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

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public List<Node> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<Node> childNodes) {
		this.childNodes = childNodes;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
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

}
