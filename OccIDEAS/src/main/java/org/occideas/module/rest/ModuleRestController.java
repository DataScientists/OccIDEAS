package org.occideas.module.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.occideas.base.rest.BaseRestController;
import org.occideas.entity.Constant;
import org.occideas.module.service.ModuleService;
import org.occideas.utilities.CommonUtil;
import org.occideas.utilities.PropUtil;
import org.occideas.vo.ModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/module")
public class ModuleRestController implements BaseRestController<ModuleVO>{

	@Autowired
	private ModuleService service;

	@GET
	@Path(value="/getlist")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response listAll() {
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		try{
			list = service.listAll();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}

	@GET
	@Path(value="/get")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response get(@QueryParam("id") Long id) {
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		if(id == -1){
			id = Long.valueOf(PropUtil.getInstance().getProperty(Constant.STUDY_INTRO));
		}
		
		try{
			list = service.findById(id);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	@GET
	@Path(value="/getinterviewmodule")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getInterviewModule(@QueryParam("id") Long id) {
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		try{
			if(id == -1){
				id = Long.valueOf(PropUtil.getInstance().getProperty(Constant.STUDY_INTRO));
			}
			list = service.findByIdForInterview(id);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	

	@Path(value="/create")
	@POST
    @Consumes(value=MediaType.APPLICATION_JSON_VALUE)
    @Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response create(ModuleVO json) {
		try{
			service.create(json);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@Path(value="/update")
	@POST
    @Consumes(value=MediaType.APPLICATION_JSON_VALUE)
    @Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response update(ModuleVO json) {
		if(CommonUtil.isReadOnlyEnabled()){
			return Response.status(Status.FORBIDDEN).build();
		}
		try{
			service.update(json);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}
	
	@Path(value="/delete")
	@POST
	public Response delete(ModuleVO json) {
		if(CommonUtil.isReadOnlyEnabled()){
			return Response.status(Status.FORBIDDEN).build();
		}
		try{
			service.merge(json);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}
	

}
