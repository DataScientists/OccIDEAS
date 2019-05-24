package org.occideas.security.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "APP_USER")
public class User implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private int id;

  @Column(name = "SSO_ID")
  private String ssoId;

  @Column(name = "PASSWORD")
  private String password;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "EMAIL")
  private String email;

  @NotEmpty
  @Column(name = "STATE")
  private String state = State.ACTIVE.getState();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "APP_USER_USER_PROFILE",
    joinColumns = {@JoinColumn(name = "USER_ID", insertable = false, updatable = false)},
    inverseJoinColumns = {@JoinColumn(name = "USER_PROFILE_ID", insertable = false, updatable = false)})
  private Set<UserProfile> userProfiles = new HashSet<UserProfile>();

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

  public Set<UserProfile> getUserProfiles() {
    return userProfiles;
  }

  public void setUserProfiles(Set<UserProfile> userProfiles) {
    this.userProfiles = userProfiles;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + ((ssoId == null) ? 0 : ssoId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof User))
      return false;
    User other = (User) obj;
    if (id != other.id)
      return false;
    if (ssoId == null) {
      return other.ssoId == null;
    } else return ssoId.equals(other.ssoId);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }
}