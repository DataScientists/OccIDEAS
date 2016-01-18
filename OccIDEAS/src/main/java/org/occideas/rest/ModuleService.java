package org.occideas.rest;

import java.io.IOException;
import java.util.List;

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

import org.hibernate.Session;
import org.occideas.HibernateUtility;
import org.occideas.Module;
import org.occideas.Node;
import org.occideas.exceptions.GenericException;
import org.occideas.mapper.ModuleMapper;
import org.occideas.rest.common.NodeCommon;

@Path("/module")
public class ModuleService extends NodeCommon
{
	public ModuleService() 
	{
		super.childClass = Module.class;
	}
	
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
	public Response getModule(@QueryParam("id") long id)
	{
		Module module;
		try {
			Session session = null;
			module = null;
			try {
				session = HibernateUtility.getSessionFactory().openSession();
				module = (Module) session.get(Module.class, id);
			} catch (Throwable e) {
				throw new GenericException("Database failure.",e);
			}
		} catch (GenericException e) {
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(module).build();
	}
	@SuppressWarnings("unchecked")
	@GET
	@Path("/getlist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getModules()
	{
		List<Module> modules = null;
		Session session = null;
		try {
			session = HibernateUtility.getSessionFactory().openSession();
			modules = (List<Module>) session.createCriteria(Module.class).list();
		} catch (Throwable e) {
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(new ModuleMapper().convertToListVO(modules)).build();
		
	}

	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Node updateModule(final Module module) throws IOException
	{
		return super.update(module);
	}
	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteModule(@QueryParam("id") long id)
	{
		super.delete(id);
	}
}
