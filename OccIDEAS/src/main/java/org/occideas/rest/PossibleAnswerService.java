package org.occideas.rest;

import java.io.IOException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.occideas.PossibleAnswer;
import org.occideas.rest.common.NodeCommon;

@Path("/possibleAnswer")
public class PossibleAnswerService extends NodeCommon{

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String createPossibleAnswer(final PossibleAnswer possibleAnswer) throws IOException
	{
		return super.create(possibleAnswer);
	}	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPossibleAnswer(@QueryParam("id") long id)
	{
		return super.get(id);
	}

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String updatePossibleAnswer(final PossibleAnswer possibleAnswer) throws IOException
	{
		return super.update(possibleAnswer);
	}
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public String deletePossibleAnswer(@QueryParam("id") long id)
	{
		return super.delete(id);
	}
}
