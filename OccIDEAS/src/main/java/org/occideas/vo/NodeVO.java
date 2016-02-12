package org.occideas.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"editEnabled","warning","placeholder","isEditing","showAgentSlider"})
public class NodeVO {

	private long idNode;
	private long anchorId;
	private String name;
	private String description;
	private String type;
	private int sequence = 1;
	private String number;
	private String parentId;
	private long link;
	private long topNodeId;
	private Date lastUpdated;
	@JsonInclude(Include.NON_NULL)
	@JsonProperty(value = "nodes")
	private List<NodeVO> childNodes;
	@JsonInclude(Include.NON_EMPTY)
	private List<NoteVO> notes;
	private long originalId;
	private Integer deleted = 0;
	private String nodeclass;
	private List<ModuleRuleVO> moduleRule;

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public List<NodeVO> getChildNodes() {
		if(childNodes==null){
			childNodes = new ArrayList<NodeVO>();
		}
		return childNodes;
	}

	public void setChildNodes(List<NodeVO> childNodes) {
		this.childNodes = childNodes;
	}

	public List<NoteVO> getNotes() {
		return notes;
	}

	public void setNotes(List<NoteVO> notes) {
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
		nodeclass = "";
		if(this.getType().length()>0){
			nodeclass = this.getType().substring(0,1);
		}
		return nodeclass;
	}

	public void setNodeclass(String nodeclass) {
		this.nodeclass = nodeclass;
	}

	public long getAnchorId() {
		if(anchorId==0){
			anchorId = idNode;
		}
		return anchorId;
	}

	public void setAnchorId(long anchorId) {
		this.anchorId = anchorId;
	}
	
	public List<ModuleRuleVO> getModuleRule() {
		return moduleRule;
	}

	public void setModuleRule(List<ModuleRuleVO> moduleRule) {
		this.moduleRule = moduleRule;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
