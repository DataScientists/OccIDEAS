package org.occideas.participantdetails.rest;

import org.occideas.base.rest.BaseRestController;
import org.occideas.entity.AssessmentIntMod;
import org.occideas.entity.ParticipantDetails;
import org.occideas.entity.ParticipantIntMod;
import org.occideas.interview.service.InterviewService;
import org.occideas.module.service.ModuleService;
import org.occideas.participant.service.ParticipantService;
import org.occideas.participantdetails.service.ParticipantDetailsService;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

@Path("/participantDetails")
public class ParticipantDetailsRestController implements BaseRestController<ParticipantDetailsVO> {

  @Autowired
  private ParticipantDetailsService service;

  @Autowired
  private ParticipantService participantService;

  @POST
  @Path(value = "/deleteList")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response deleteList() {

    String participantId = "1";
    String startsWith = "R3";
    try {
      List<ParticipantVO> list = participantService.findById(Long.valueOf(participantId));
      ParticipantVO participant = list.get(0);
      List<ParticipantDetailsVO> detailsVOList = getObjectsWithNameStarting(participant.getParticipantDetails(), startsWith);
      service.deleteList(detailsVOList);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }
  public List<ParticipantDetailsVO> getObjectsWithNameStarting(List<ParticipantDetailsVO> items, String prefix) {
    List<ParticipantDetailsVO> filteredItems = new ArrayList<>();

    for (ParticipantDetailsVO item : items) {
      if (item.getDetailName().startsWith(prefix)) {
        filteredItems.add(item);
      }
    }
    return filteredItems;
  }

  @Override
  public Response listAll() {
    return null;
  }

  @Override
  public Response get(Long id) {
    return null;
  }

  @Override
  public Response create(ParticipantDetailsVO json) {
    return null;
  }

  @Override
  public Response update(ParticipantDetailsVO json) {
    return null;
  }

  @Override
  public Response delete(ParticipantDetailsVO json) {
    return null;
  }
}

