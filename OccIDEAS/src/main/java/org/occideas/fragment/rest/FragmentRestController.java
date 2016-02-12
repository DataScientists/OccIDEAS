package org.occideas.fragment.rest;

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
import org.occideas.fragment.service.FragmentService;
import org.occideas.vo.FragmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/fragment")
public class FragmentRestController implements BaseRestController<FragmentVO>{

	@Autowired
	private FragmentService service;

	@GET
	@Path(value="/getlist")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response listAll() {
		List<FragmentVO> list = new ArrayList<FragmentVO>();
		try{
			list = service.listAll();
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}

	@GET
	@Path(value="/get")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response get(@QueryParam("id") Long id) {
		List<FragmentVO> list = new ArrayList<FragmentVO>();
		try{
			list = service.findById(id);
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}

	@Path(value="/create")
	@POST
    @Consumes(value=MediaType.APPLICATION_JSON_VALUE)
    @Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response create(FragmentVO json) {
		try{
			service.createFragment(json);
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@Path(value="/update")
	@POST
    @Consumes(value=MediaType.APPLICATION_JSON_VALUE)
    @Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response update(FragmentVO json) {
		try{
			service.update(json);
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@Path(value="/delete")
	@POST
	public Response delete(FragmentVO json) {
		try{
			service.merge(json);
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}
	

}
