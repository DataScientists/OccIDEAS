package org.occideas.modulefragment.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.occideas.modulefragment.service.ModuleFragmentService;
import org.occideas.vo.ModuleFragmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/modulefragment")
public class ModuleFragmentRestController {

	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ModuleFragmentService service;

	@GET
	@Path(value = "/getModuleFragmentByModuleId")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getModuleFragmentByModuleId(@QueryParam("id") Long id){
		List<ModuleFragmentVO> list = new ArrayList<>();
		try {
			list = service.getModuleFragmentByModuleId(id);
		} catch (Throwable e) {
			log.error("Error encountered in module fragment rest -", e);
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
}
