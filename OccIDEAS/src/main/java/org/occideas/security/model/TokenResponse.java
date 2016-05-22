package org.occideas.security.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
    @JsonProperty
    private String token;
    @JsonProperty
    private Map<String,Object> userInfo = new HashMap<>();
    @JsonProperty
    private HashMapExt facRoleDDValues =  new HashMapExt();
    @JsonProperty
    private String[] facDDVals;
	@JsonProperty
    private String[] roleDDVals;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Map<String, Object> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Map<String, Object> userInfo) {
		this.userInfo = userInfo;
	}
	
    public HashMapExt getFacRoleDDValues() {
		return facRoleDDValues;
	}

	public void setFacRoleDDValues(HashMapExt facRoleDDValues) {
		this.facRoleDDValues = facRoleDDValues;
	}
	
    public String[] getFacDDVals() {
		return facDDVals;
	}

	public void setFacDDVals(String[] facDDVals) {
		this.facDDVals = new String[facDDVals.length];
		this.facDDVals = facDDVals;
	}

	public String[] getRoleDDVals() {
		return roleDDVals;
	}

	public void setRoleDDVals(String[] roleDDVals) {
		this.roleDDVals = new String[roleDDVals.length];
		this.roleDDVals = roleDDVals;
	}
    
}
