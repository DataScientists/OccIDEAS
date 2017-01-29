package org.occideas.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties({"editEnabled","info","warning","placeholder","isEditing","showAgentSlider","id","collapsed","isOpen","isSelected"})
public abstract class NodeVO {

	protected long idNode;
	protected long anchorId;
	protected String name;
	protected String description;
	protected String type;
	protected int sequence = 1;
	protected String number;
	protected String parentId;
	protected long link;
	protected long topNodeId;
	protected Date lastUpdated;
	@JsonInclude(Include.NON_EMPTY)
	protected List<NoteVO> notes;
	protected long originalId;
	protected Integer deleted = 0;
	protected String nodeclass;
	@JsonInclude(Include.NON_NULL)
	protected List<ModuleRuleVO> moduleRule;

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
		if(this.getType()!=null){
			if(this.getType().length()>0){
				nodeclass = this.getType().substring(0,1);
			}
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
		if(moduleRule==null){
			moduleRule = new ArrayList<ModuleRuleVO>();
		}
		return moduleRule;
	}

	public void setModuleRule(List<ModuleRuleVO> moduleRule) {
		this.moduleRule = moduleRule;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (idNode ^ (idNode >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nodeclass == null) ? 0 : nodeclass.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
		result = prime * result + (int) (topNodeId ^ (topNodeId >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeVO other = (NodeVO) obj;
		if (deleted == null) {
			if (other.deleted != null)
				return false;
		} else if (!deleted.equals(other.deleted))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idNode != other.idNode)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nodeclass == null) {
			if (other.nodeclass != null)
				return false;
		} else if (!nodeclass.equals(other.nodeclass))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (parentId == null) {
			if (other.parentId != null)
				return false;
		} else if (!parentId.equals(other.parentId))
			return false;
		if (topNodeId != other.topNodeId)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	

}
