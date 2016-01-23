package org.occideas.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class NoteVO {

	private long idNote;
	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty(value = "nodes")
	private NodeVO nodeVO;
	private String type;
	private String text;
	private Date lastUpdated;
	private Integer deleted;

	public long getIdNote() {
		return idNote;
	}

	public void setIdNote(long idNote) {
		this.idNote = idNote;
	}

	public NodeVO getNodeVO() {
		return nodeVO;
	}

	public void setNodeVO(NodeVO nodeVO) {
		this.nodeVO = nodeVO;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

}
