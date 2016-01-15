package org.occideas.rest.response;

import java.io.Serializable;

public class StatusOnly  implements Serializable 
{
	protected String status;
	
	public StatusOnly()
	{
		super();
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	public String getStatus()
	{
		return this.status;		
	}
}
