package org.occideas.modulerule.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.occideas.base.rest.BaseRestController;
import org.occideas.modulerule.service.ModuleRuleService;
import org.occideas.vo.ModuleRuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/modulerule")
public class ModuleRuleRestController implements BaseRestController<ModuleRuleVO>{

	@Autowired
	private ModuleRuleService service;

	@GET
	@Path(value="/getlist")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response listAll() {
		List<ModuleRuleVO> list = new ArrayList<ModuleRuleVO>();
		try{
			list = service.listAll();
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@GET
	@Path(value="/getbymoduleid")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getByModuleId(@QueryParam("id") Long id) {
		List<ModuleRuleVO> list = new ArrayList<ModuleRuleVO>();
		try{
			list = service.findByModuleId(id);
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	@GET
	@Path(value="/getbyagentid")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getByAgentId(@QueryParam("id") Long id) {
		List<ModuleRuleVO> list = new ArrayList<ModuleRuleVO>();
		try{
			list = service.findByAgentId(id);
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}

	@Override
	public Response get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response create(ModuleRuleVO json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response update(ModuleRuleVO json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response delete(ModuleRuleVO json) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

}
