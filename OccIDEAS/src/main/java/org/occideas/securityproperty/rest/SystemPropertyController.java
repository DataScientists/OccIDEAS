package org.occideas.securityproperty.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/systemproperty")
public class SystemPropertyController {

	@Autowired
	private SystemPropertyService service;

	@GET
	@Path(value = "/getById")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getById(@QueryParam("id") String id) {
		SystemPropertyVO sysProp = null;
		try {
			sysProp = service.getById(id);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(sysProp).build();
	}
	
	@POST
	@Path(value = "/save")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response save(SystemPropertyVO vo) {
		SystemPropertyVO sysProp = null;
		try {
			sysProp = service.save(vo);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(sysProp).build();
	}
	
}
