package org.occideas.vo;

import java.util.HashSet;
import java.util.Set;

import org.occideas.security.model.State;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UserVO {

	private int id;
	private String ssoId;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String state = State.ACTIVE.getState();
	private Set<UserProfileVO> userProfiles = new HashSet<UserProfileVO>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSsoId() {
		return ssoId;
	}

	public void setSsoId(String ssoId) {
		this.ssoId = ssoId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Set<UserProfileVO> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(Set<UserProfileVO> userProfiles) {
		this.userProfiles = userProfiles;
	}

	@Override
	public String toString() {
		return "UserVO [id=" + id + ", ssoId=" + ssoId + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", state=" + state + ", userProfiles=" + userProfiles
				+ "]";
	}

}
