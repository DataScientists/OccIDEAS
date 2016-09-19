package org.occideas.reporthistory.rest;

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

import org.occideas.reporthistory.service.ReportHistoryService;
import org.occideas.vo.ReportHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/reportHistory")
public class ReportHistoryRestController {

	@Autowired
	private ReportHistoryService service;
	
	@GET
	@Path(value="/getAll")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getAll() {
		List<ReportHistoryVO> list = new ArrayList<>();
		try{
			list = service.getAll();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@GET
	@Path(value="/getByType")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getByType(@QueryParam("type") String type) {
		List<ReportHistoryVO> list = new ArrayList<>();
		try{
			list = service.getByType(type);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@Path(value="/save")
	@POST
    @Consumes(value=MediaType.APPLICATION_JSON_VALUE)
    @Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response save(ReportHistoryVO vo) {
		try{
			service.save(vo);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}
	
	@Path(value="/delete")
	@POST
    @Consumes(value=MediaType.APPLICATION_JSON_VALUE)
    @Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response delete(ReportHistoryVO vo) {
		try{
			service.delete(vo);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}
	
}
