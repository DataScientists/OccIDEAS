package org.occideas.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="interview_question")
public class InterviewQuestion {

	@Id
	@Column(name="id")
	private BigInteger id;
	
	@Column(name="idinterview")
	private BigInteger idInterview;
	
	@Column(name="referenceNumber")
	private String referenceNumber;
	
	@Column(name="idNode")
	private BigInteger idNode;
	
	@Column(name="name")
	private String name;
	
	@Column(name="description")
	private String description;
	
	@Column(name="nodeClass")
	private String nodeClass;
	
	@Column(name="number")
	private String number;

	@Column(name="type")
	private String type;
	
	@Column(name="deleted")
	private Integer deleted;
	
	@Column(name="lastUpdated")
	private Timestamp lastUpdated;
}
