package org.occideas.rest;

import java.io.IOException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.occideas.Question;
import org.occideas.rest.common.NodeCommon;

@Path("/question")
public class QuestionService extends NodeCommon{

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String createQuestion(final Question question) throws IOException
	{
		return super.create(question);
	}	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public String getQuestion(@QueryParam("id") long id)
	{
		return super.get(id);
	}

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String updateQuestion(final Question question) throws IOException
	{
		return super.update(question);
	}
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteQuestion(@QueryParam("id") long id)
	{
		return super.delete(id);
	}
}
