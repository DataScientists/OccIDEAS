package org.occideas.possibleanswer.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.occideas.base.rest.BaseRestController;
import org.occideas.possibleanswer.service.PossibleAnswerService;
import org.occideas.vo.PossibleAnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/answer")
public class PossibleAnswerRestController implements BaseRestController<PossibleAnswerVO>{

	@Autowired
	private PossibleAnswerService service;	
	
	@Override
	public Response listAll() {
		return null;
	}

	@GET
	@Path(value="/getById")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	@Override
	public Response get(@QueryParam("id") Long id) {
		List<PossibleAnswerVO> list = new ArrayList<PossibleAnswerVO>();
		try{
			list = service.findById(id);
		}catch(Throwable e){
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}

	@Override
	public Response create(PossibleAnswerVO json) {
		return null;
	}

	@Override
	public Response update(PossibleAnswerVO json) {
		return null;
	}

	@Override
	public Response delete(PossibleAnswerVO json) {
		return null;
	}

	
	
}
