package org.occideas.rule.rest;

import org.occideas.base.rest.BaseRestController;
import org.occideas.rule.service.RuleService;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

@Path("/rule")
public class RuleRestController implements BaseRestController<RuleVO> {

  @Autowired
  private RuleService service;

  @GET
  @Path(value = "/getlist")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response listAll() {
    List<RuleVO> list = new ArrayList<RuleVO>();
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
    List<RuleVO> list = new ArrayList<RuleVO>();
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
  public Response create(RuleVO json) {
    RuleVO rule = new RuleVO();
    try {
      rule = service.create(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(rule).build();
  }

  @Path(value = "/saveOrUpdate")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response saveOrUpdate(RuleVO json) {
    try {
      service.saveOrUpdate(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }

  @Path(value = "/saveList")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response saveList(List<RuleVO> json) {
    try {
      for (RuleVO rule : json) {
        service.saveOrUpdate(rule);
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }

  @Path(value = "/delete")
  @POST
  public Response delete(RuleVO json) {
    try {
      service.delete(json);
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
  @Override
  public Response update(RuleVO json) {
    try {
      service.update(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }


}
