package org.occideas.participant.rest;

import org.occideas.base.rest.BaseRestController;
import org.occideas.entity.AssessmentIntMod;
import org.occideas.entity.Node;
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

@Path("/participant")
public class ParticipantRestController implements BaseRestController<ParticipantVO> {

  //private Logger log = LogManager.getLogger(this.getClass());

  @Autowired
  private ParticipantService service;

  @Autowired
  private InterviewService interviewService;

  @Autowired
  private ModuleService moduleService;

  @Autowired
  private ParticipantDetailsService participantDetailsService;

  @GET
  @Path(value = "/getByReferenceNumber")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getByReferenceNumber(@QueryParam("referenceNumber") String referenceNumber) {
    ParticipantVO participant = new ParticipantVO();
    try {
      participant = service.getByReferenceNumber(referenceNumber);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(participant).build();
  }

	@GET
	@Path(value = "/getByReferenceNumberPrefix")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getByReferenceNumberPrefix(@QueryParam("referenceNumberPrefix") String referenceNumber) {
		List<ParticipantVO> list = new ArrayList<ParticipantVO>();
		try {
			list = service.getByReferenceNumberPrefix(referenceNumber);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
  
  @GET
  @Path(value = "/getlist")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response listAll() {
    List<ParticipantVO> list = new ArrayList<ParticipantVO>();
    try {
      list = service.listAllParticipantWithInt();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @POST
  @Path(value = "/getPaginatedParticipantWithModList")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getPaginatedParticipantWithModList(AssessmentFilterVO filterVO) {
    PageVO<ParticipantIntMod> page = null;
    try {
      page = service.getPaginatedParticipantWithModList(filterVO.getPageNumber(), filterVO.getSize(), filterVO);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(page).build();
  }

  @POST
  @Path(value = "/getPaginatedAssessmentWithModList")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getPaginatedAssessmentWithModList(AssessmentFilterVO filterVO) {
    PageVO<AssessmentIntMod> page = null;
    try {
      page = service.getPaginatedAssessmentWithModList(filterVO.getPageNumber(), filterVO.getSize(), filterVO);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(page).build();
  }

  @POST
  @Path(value = "/getPaginatedParticipantList")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getPaginatedParticipant(ParticipantFilterVO filterVO) {
    PageVO<ParticipantIntMod> page = null;
    try {
      page = service.getPaginatedParticipantList(filterVO.getPageNumber(), filterVO.getSize(), filterVO);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(page).build();
  }

  @GET
  @Path(value = "/get")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response get(@QueryParam("id") Long id) {
    List<ParticipantVO> list = new ArrayList<ParticipantVO>();
    try {
      list = service.findById(id);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/getinterviewparticipant")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getInterviewParticipant(@QueryParam("id") Long id) {
    List<ParticipantVO> list = new ArrayList<ParticipantVO>();
    try {
      list = service.findByIdForInterview(id);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @Path(value = "/createAMRBatch")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response createAMRBatch(List<ParticipantVO> json) {
    
    try {
    	for (ParticipantVO participantVO : json) {
    		ParticipantVO participantVOCreated = service.create(participantVO);
    		InterviewVO	childInterviewVO = new InterviewVO();
    		childInterviewVO.setParticipant(participantVOCreated);
    		childInterviewVO.setReferenceNumber(participantVO.getReference());
    		childInterviewVO = interviewService.create(childInterviewVO);
    		
    		List<NoteVO> notes = new ArrayList<>();
            NoteVO noteVO = new NoteVO();
            noteVO.setDeleted(0);
            noteVO.setInterviewId(childInterviewVO.getInterviewId());
            noteVO.setText(participantVO.getNotes().get(0).getText());
            noteVO.setType("AMR");
            notes.add(noteVO);
            if(participantVO.getNotes().size()>=2) {
            	NoteVO noteVO2 = new NoteVO();
                noteVO2.setDeleted(0);
                noteVO2.setInterviewId(childInterviewVO.getInterviewId());
                noteVO2.setText(participantVO.getNotes().get(1).getText());
                noteVO2.setType("AMRSurveyLink");
                notes.add(noteVO2);
            }
            
			childInterviewVO.setNotes(notes);
			interviewService.update(childInterviewVO);
			
			
    	}
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }
  
  @Path(value = "/create")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response create(ParticipantVO json) {
    ParticipantVO participantVO;
    try {
      participantVO = service.create(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(participantVO).build();
  }

  @Path(value = "/update")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response update(ParticipantVO json) {
    try {
      service.update(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }

  @Path(value = "/delete")
  @POST
  public Response delete(ParticipantVO json) {
    json.setDeleted(1);
    try {
      service.update(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }

  @POST
  @Path(value = "/nextquestion")
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getNextQuestion(ParticipantVO participant) {
    participant = service.findByIdForInterview(participant.getIdParticipant()).get(0);
    try {
      QuestionVO questionVO = getNextInterviewQuestion(participant);
      if (questionVO != null) {
        return Response.ok(questionVO).build();
      } else {
        for (InterviewVO interviewVO : participant.getInterviews()) {
          this.determineFiredRules(interviewVO);

        }
        return Response.status(Response.Status.NO_CONTENT).build();
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
  }

  @GET
  @Path(value = "/checkIfStudyAgentPreLoaded")
  public Boolean checkIfStudyAgentPreLoaded() {
    return service.checkIfStudyAgentPreLoaded();
  }

  private void determineFiredRules(InterviewVO interview) {
    ArrayList<RuleVO> firedRules = new ArrayList<RuleVO>();
    ArrayList<RuleVO> rules = new ArrayList<RuleVO>();
    if (interview.getModule() != null) {
      ModuleVO module = null;
      List<ModuleVO> moduleVOList = moduleService.findById(interview.getModule().getIdNode());
      for (ModuleVO m : moduleVOList) {
        module = m;
      }

      for (ModuleRuleVO moduleRule : module.getModuleRule()) {
        rules.add(moduleRule.getRule());
      }
      rules = removeDuplicates(rules);

    } else if (interview.getFragment() != null) {
      FragmentVO module = interview.getFragment();
      for (ModuleRuleVO moduleRule : module.getModuleRule()) {
        rules.add(moduleRule.getRule());
      }
      //remove duplicates
      rules = removeDuplicates(rules);
    }
    for (RuleVO rule : rules) {
      boolean bFired = false;
      for (PossibleAnswerVO pa : rule.getConditions()) {
//				for(InterviewQuestionAnswerVO iqa: interview.getQuestionsAsked()){
//					if(iqa.getPossibleAnswer()!=null){
//						if(pa.getIdNode()==iqa.getPossibleAnswer().getIdNode()){
//	    	    			bFired = true;
//	    	    			break;
//	    	    		}else{
//	    	    			bFired = false;
//	    	    		}
//					}	    		
//    	    	}
      }
      if (bFired) {
        firedRules.add(rule);
      }
    }
    firedRules = removeDuplicates(firedRules);
    interview.setFiredRules(firedRules);
    interviewService.update(interview);
    if (!(interview.getInterviews().isEmpty())) {
      for (InterviewVO childInterview : interview.getInterviews()) {
        this.determineFiredRules(childInterview);
      }
    }
  }

  private ArrayList<RuleVO> removeDuplicates(List<RuleVO> rules) {
    ArrayList<RuleVO> retValue = new ArrayList<RuleVO>();
    for (RuleVO rule : rules) {
      if (!retValue.contains(rule)) {
        retValue.add(rule);
      }
    }
    return retValue;
  }

  private QuestionVO getNextInterviewQuestion(ParticipantVO participant) {
    QuestionVO questionVO = null;
    for (InterviewVO interviewVO : participant.getInterviews()) {
      questionVO = this.getNearestQuestion(interviewVO);
    }
    return questionVO;
  }

  private QuestionVO getNearestQuestion(InterviewVO interviewVO) {
    QuestionVO questionVO = null;
    //first check child interviews
    if (!(interviewVO.getInterviews().isEmpty())) { //has child interviews
      for (InterviewVO childInterview : interviewVO.getInterviews()) {
        questionVO = getNearestQuestion(childInterview);
        if (questionVO != null) {
          questionVO.setActiveInterviewId(childInterview.getInterviewId());
        }

      }
    }
    return questionVO;
  }
  @POST
  @Path(value = "/deleteList")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response deleteList(@QueryParam("participantId") String participantId,@QueryParam("startsWith") String startsWith) {

    //String participantId = "1";
    //String startsWith = "R3";
    try {
      List<ParticipantVO> list = service.findById(Long.valueOf(participantId));
      ParticipantVO participant = list.get(0);
      List<ParticipantDetailsVO> detailsVOList = getObjectsWithNameStarting(participant.getParticipantDetails(), startsWith);
      participantDetailsService.deleteList(detailsVOList);
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
}

