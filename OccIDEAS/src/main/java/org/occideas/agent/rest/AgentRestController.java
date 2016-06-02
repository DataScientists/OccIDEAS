package org.occideas.agent.rest;

import org.occideas.agent.service.AgentService;
import org.occideas.base.rest.BaseRestController;
import org.occideas.rule.service.RuleService;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.AgentVO;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

@Path("/agent")
public class AgentRestController implements BaseRestController<AgentVO>{

	@Autowired
	private AgentService service;

    @Autowired
    private RuleService ruleService;

	@GET
	@Path(value="/getlist")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response listAll() {
		List<AgentVO> list = new ArrayList<AgentVO>();
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
		List<AgentVO> list = new ArrayList<AgentVO>();
		try{
			list = service.findById(id);
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
	public Response create(AgentVO json) {
		if(CommonUtil.isReadOnlyEnabled()){
			return Response.status(Status.FORBIDDEN).build();
		}
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
	public Response update(AgentVO json) {
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
	public Response delete(AgentVO json) {
		if(CommonUtil.isReadOnlyEnabled()){
			return Response.status(Status.FORBIDDEN).build();
		}
		try{
			service.delete(json);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

    @GET
    @Path(value="/hasrules")
    public Response hasRules(@QueryParam("agentId") Long agentId) {
        boolean hasRule = false;
        try{
            List<RuleVO> list = ruleService.findByAgentId(agentId);
            if(list != null && !list.isEmpty()){
                hasRule = true;
            }
        }catch(Throwable e){
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(hasRule).build();
    }

}
