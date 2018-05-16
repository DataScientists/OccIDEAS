package org.occideas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class JMXLog
{

    @Id 
    @GeneratedValue
    private long id;
    private String sessionId;
    private String userId;
    private String function;
    private String header;
    private String getParameters;
    private String postParameters;
    private Integer deleted;
    private Date createdDate;

    public long getId()
    {
        return id;
    }


    public void setId(long id)
    {
        this.id = id;
    }


    public String getSessionId()
    {
        return sessionId;
    }


    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }


    public String getFunction()
    {
        return function;
    }


    public void setFunction(String function)
    {
        this.function = function;
    }


    public String getHeader()
    {
        return header;
    }


    public void setHeader(String header)
    {
        this.header = header;
    }


    public String getGetParameters()
    {
        return getParameters;
    }


    public void setGetParameters(String getParameters)
    {
        this.getParameters = getParameters;
    }


    public String getPostParameters()
    {
        return postParameters;
    }


    public void setPostParameters(String postParameters)
    {
        this.postParameters = postParameters;
    }


    public Integer getDeleted()
    {
        return deleted;
    }


    public void setDeleted(Integer deleted)
    {
        this.deleted = deleted;
    }


    public Date getCreatedDate()
    {
        return createdDate;
    }


    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }


    public String getUserId()
    {
        return userId;
    }


    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    
}
