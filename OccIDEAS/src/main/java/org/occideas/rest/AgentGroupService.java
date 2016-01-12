package org.occideas.rest;

import java.io.IOException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.occideas.AgentGroup;
import org.occideas.rest.common.AgentCommon;

@Path("/agentagentGroup")
public class AgentGroupService extends AgentCommon{
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String createAgentGroup(final AgentGroup agentGroup) throws IOException
	{
		return super.create(agentGroup);
	}	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAgentGroup(@QueryParam("id") long id)
	{
		return super.get(id);
	}

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String updateAgentGroup(final AgentGroup agentGroup) throws IOException
	{
		return super.update(agentGroup);
	}
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAgentGroup(@QueryParam("id") long id)
	{
		return super.delete(id);
	}
}
