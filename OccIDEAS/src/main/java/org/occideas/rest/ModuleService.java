package org.occideas.rest;

import java.io.IOException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.occideas.Module;
import org.occideas.rest.common.NodeCommon;

@Path("/module")
public class ModuleService extends NodeCommon{

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String createModule(final Module module) throws IOException
	{
		return super.create(module);
	}	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public String getModule(@QueryParam("id") long id)
	{
		return super.get(id);
	}
	@GET
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public String getModules()
	{
		return super.getAllModules();
	}

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public String updateModule(final Module module) throws IOException
	{
		return super.update(module);
	}
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteModule(@QueryParam("id") long id)
	{
		return super.delete(id);
	}
}
