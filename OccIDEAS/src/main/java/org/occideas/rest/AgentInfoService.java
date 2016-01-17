package org.occideas.rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.occideas.AgentGroup;
import org.occideas.AgentInfo;
import org.occideas.exceptions.GenericException;
import org.occideas.rest.common.AgentCommon;


@Path("/agentinfo")
public class AgentInfoService extends AgentCommon{
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String createAgentInfo(final AgentInfo info) throws IOException
	{
		return super.create(info);
	}	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAgentInfo(@QueryParam("id") long id)
	{
		AgentGroup agentGroup;
		try {
			agentGroup = super.get(id);
		} catch (GenericException e) {
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(agentGroup).build();
	}

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String updateAgentInfo(final AgentInfo info) throws IOException
	{
		return super.update(info);
	}
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAgentInfo(@QueryParam("id") long id)
	{
		return super.delete(id);
	}
}
