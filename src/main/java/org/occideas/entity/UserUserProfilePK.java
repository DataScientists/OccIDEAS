package org.occideas.entity;

import java.io.Serializable;

public class UserUserProfilePK implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  protected int userId;
  protected int userProfileId;

  public UserUserProfilePK() {
  }

  public UserUserProfilePK(int userId) {
    this.userId = userId;
  }

  public UserUserProfilePK(int userId, int userProfileId) {
    this.userId = userId;
    this.userProfileId = userProfileId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + userId;
    result = prime * result + userProfileId;
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
    UserUserProfilePK other = (UserUserProfilePK) obj;
    if (userId != other.userId)
      return false;
    return userProfileId == other.userProfileId;
  }

}
