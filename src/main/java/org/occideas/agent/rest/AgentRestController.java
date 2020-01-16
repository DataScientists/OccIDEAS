package org.occideas.agent.rest;

import org.occideas.agent.service.AgentService;
import org.occideas.base.rest.BaseRestController;
import org.occideas.interview.service.InterviewService;
import org.occideas.rule.service.RuleService;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/agent")
public class AgentRestController implements BaseRestController<AgentVO> {

  @Autowired
  private AgentService service;

  @Autowired
  private RuleService ruleService;

  @Autowired
  private InterviewService interviewService;

  @Autowired
  private SystemPropertyService propService;


  @GET
  @Path(value = "/getlist")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response listAll() {
    List<AgentVO> list = new ArrayList<AgentVO>();
    try {
      list = service.listAll();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/get")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response get(@QueryParam("id") Long id) {
    List<AgentVO> list = new ArrayList<AgentVO>();
    try {
      list = service.findById(id);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @Path(value = "/create")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response create(AgentVO json) {
    try {
      service.create(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }

  @Path(value = "/update")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response update(AgentVO json) {
    try {
      long id = service.updateAndGetId(json);
      return Response.ok(id).build();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
  }

  @Path(value = "/saveAgentGroup")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response updateAgentGroup(AgentGroupVO json) {
    try {
      service.saveAgentGroup(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }

  @Path(value = "/updateStudyAgents")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response updateStudyAgents(AgentVO json) {
    try {
      service.updateStudyAgents(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }

  @Path(value = "/deleteStudyAgents")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response deleteStudyAgents(SystemPropertyVO vo) {
    try {
      service.deleteStudyAgents(vo);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }

  @Path(value = "/loadStudyAgents")
  @GET
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response loadStudyAgents() {
    List<SystemPropertyVO> list = null;
    try {
      list = service.loadStudyAgents();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @Path(value = "/getstudyagents")
  @GET
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getStudyAgents() {
    List<AgentVO> list = null;
    try {
      list = service.getStudyAgents();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @Path(value = "/getstudyagentswithrules")
  @POST
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  /**
   * For displaying initial agents table
   * @param interviewId
   * @return
   */
  public Response getStudyAgentsWithRules(@QueryParam("interviewId") Long interviewId) {

    Map<Long, AgentVO> agentMap = new HashMap<>();

    try {
      List<SystemPropertyVO> agents = propService.getByType("studyagent");

      InterviewVO vo = interviewService.findInterviewWithFiredRulesById(interviewId);

      for (SystemPropertyVO agent : agents) {
        for (RuleVO rule : vo.getFiredRules()) {
          if (Long.valueOf(agent.getValue()) == rule.getAgentId()) {
            if (agentMap.containsKey(rule.getAgentId())) {
              agentMap.get(rule.getAgentId()).setTotal(agentMap.get(rule.getAgentId()).getTotal() + 1);
            } else {
              AgentVO agentVo = new AgentVO();
              agentVo.setIdAgent(rule.getAgentId());
              agentVo.setName(agent.getName());
              agentMap.put(rule.getAgentId(), agentVo);
            }
          }
        }
      }

    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(agentMap.values()).build();
  }

  @Path(value = "/delete")
  @POST
  public Response delete(AgentVO json) {
    try {
      service.delete(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }

  @GET
  @Path(value = "/hasrules")
  public Response hasRules(@QueryParam("agentId") Long agentId) {
    boolean hasRule = false;
    try {
      List<RuleVO> list = ruleService.findByAgentId(agentId);
      if (list != null && !list.isEmpty()) {
        hasRule = true;
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(hasRule).build();
  }

  @GET
  @Path(value = "/find")
  public Response findAgent(@QueryParam("agentId") Long agentId) {
    AgentVO agent = null;
    try {
      List<AgentVO> list = service.findById(agentId);
      if (list != null && !list.isEmpty()) {
        agent = list.get(0);
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(agent).build();
  }

  @GET
  @Path(value = "/getrules")
  public Response getrules(@QueryParam("agentId") Long agentId) {
    List<RuleVO> list = new ArrayList<RuleVO>();
    try {
      list = ruleService.findByAgentId(agentId);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

}
