package org.occideas.security.model;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUDIT_LOG")
public class AuditLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private long id;
	@Column(name = "username")
	private String username;
	@Column(name = "user_type")
	private String userType;
	@Column(name = "action")
	private String action;
	@Column(name = "method")
	private String method;
	@Column(name = "arguments")
	private byte[] arguments;
	@Column(name = "date")
	private Timestamp date;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public byte[] getArguments() {
		return arguments;
	}

	public void setArguments(byte[] arguments) {
		this.arguments = arguments;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}
