package org.occideas.security.model;

public enum UserProfileType {
	READONLY("READONLY"),
    ASSESSOR("ASSESSOR"),
    CONTDEV("CONTDEV"),
    INTERVIEWER("INTERVIEWER");
     
    String userProfileType;
     
    private UserProfileType(String userProfileType){
        this.userProfileType = userProfileType;
    }
     
    public String getUserProfileType(){
        return userProfileType;
    }
     
}