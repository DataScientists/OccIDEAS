package org.occideas.moduleintromodule.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.occideas.moduleintromodule.service.ModuleIntroModuleService;
import org.occideas.vo.ModuleIntroModuleVO;
import org.occideas.vo.ModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/moduleintromodule")
public class ModuleIntroModuleRestController {

	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ModuleIntroModuleService service;

	@GET
	@Path(value = "/getModuleIntroModuleByModuleId")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getModuleIntroModuleByModuleId(@QueryParam("id") Long id){
		List<ModuleIntroModuleVO> list = new ArrayList<>();
		try {
			list = service.getModuleIntroModuleByModuleId(id);
		} catch (Throwable e) {
			log.error("Error encountered in module intro rest -", e);
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@GET
	@Path(value = "/getWithFragments")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response get(@QueryParam("id") Long id) {
		List<ModuleVO> list = new ArrayList<>();
		try {
			list = service.findByIdWithFragments(id);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
}
