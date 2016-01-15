package org.occideas.rest.response;

import java.io.Serializable;

public class DataResponse  extends StatusOnly 
{
	private Object data;
	
	public DataResponse()
	{
		super();
	}
	
	public void setData(Object data)
	{
		this.data = data;
	}
	
	public Object getData()
	{
		return this.data;		
	}
}
