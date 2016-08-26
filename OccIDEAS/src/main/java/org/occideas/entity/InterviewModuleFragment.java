package org.occideas.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
@Table(name = "InterviewModule_Fragment")
public class InterviewModuleFragment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String primaryKey;
	private long idFragment;
	private String fragmentNodeName;
	private String interviewId;
	private String interviewFragmentName;
	private long idNode;
	private String nodeNumber;

	public long getIdFragment() {
		return idFragment;
	}

	public void setIdFragment(long idFragment) {
		this.idFragment = idFragment;
	}

	public String getFragmentNodeName() {
		return fragmentNodeName;
	}

	public void setFragmentNodeName(String fragmentNodeName) {
		this.fragmentNodeName = fragmentNodeName;
	}

	public String getInterviewId() {
		return interviewId;
	}

	public void setInterviewId(String interviewId) {
		this.interviewId = interviewId;
	}

	public String getInterviewFragmentName() {
		return interviewFragmentName;
	}

	public void setInterviewFragmentName(String interviewFragmentName) {
		this.interviewFragmentName = interviewFragmentName;
	}

	public long getIdNode() {
		return idNode;
	}

	public void setIdNode(long idNode) {
		this.idNode = idNode;
	}

	public String getNodeNumber() {
		return nodeNumber;
	}

	public void setNodeNumber(String nodeNumber) {
		this.nodeNumber = nodeNumber;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

}
