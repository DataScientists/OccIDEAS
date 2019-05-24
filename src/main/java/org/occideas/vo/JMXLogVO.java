package org.occideas.vo;

import java.util.Date;

public class JMXLogVO implements Comparable<JMXLogVO>
{
    private long id;
    private String sessionId;
    private String userId;
    private String function;
    private String header;
    private String getParameters;
    private String postParameters;
    private Integer deleted;
    private Date createdDate;


    public JMXLogVO()
    {}


    public JMXLogVO(long id, String sessionId, String userId, String function, String header, String getParameters, String postParameters, Integer deleted)
    {
        super();
        this.id = id;
        this.sessionId = sessionId;
        this.userId = userId;
        this.function = function;
        this.header = header;
        this.getParameters = getParameters;
        this.postParameters = postParameters;
        this.deleted = deleted;
    }


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


    public String getUserId()
    {
        return userId;
    }


    public void setUserId(String userId)
    {
        this.userId = userId;
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


    @Override
    public int compareTo(JMXLogVO o)
    {
        return o.getId() > this.getId() ? -1 : o.getId() < this.getId() ? 1 : 0;
    }


    @Override
    public String toString()
    {
        return "JMXLogVO [id="
            + id + ", sessionId=" + sessionId + ", userId=" + userId + ", function=" + function + ", header=" + header + ", getParameters=" + getParameters + ", postParameters="
            + postParameters + ", deleted=" + deleted + ", createdDate=" + createdDate + "]";
    }

}
