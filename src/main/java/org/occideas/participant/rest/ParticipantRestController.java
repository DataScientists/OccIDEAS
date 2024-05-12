package org.occideas.participant.rest;

import org.occideas.base.rest.BaseRestController;
import org.occideas.entity.AssessmentIntMod;
import org.occideas.entity.Node;
import org.occideas.entity.ParticipantIntMod;
import org.occideas.interview.service.InterviewService;
import org.occideas.module.service.ModuleService;
import org.occideas.participant.service.ParticipantService;
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
//	private QuestionVO getNextInterviewQuestionFromLinkedInterview(QuestionVO questionVO,InterviewVO interviewVO) {
//		QuestionVO retValue = null;
//		String linkedModuleName = "";
//		//create a new child interview			
//		InterviewVO	childInterviewVO = new InterviewVO();
//		childInterviewVO.setReferenceNumber(interviewVO.getParticipant().getReference());
//		childInterviewVO.setParentId(questionVO.getActiveInterviewId());
//		childInterviewVO.setParticipant(interviewVO.getParticipant());
//		if(questionVO.getType().equalsIgnoreCase("Q_linkedmodule")){
//			for(ModuleVO module:moduleService.findByIdForInterview(questionVO.getLink())){
//				childInterviewVO.setModule(module);
//				linkedModuleName = "Module:"+module.getIdNode()+":"+module.getName();
//			}
//		} else if(questionVO.getType().equalsIgnoreCase("Q_linkedajsm")){
//			for(FragmentVO fragment:fragmentService.findByIdForInterview(questionVO.getLink())){
//				childInterviewVO.setFragment(fragment);
//				linkedModuleName = "AJSM:"+fragment.getIdNode()+":"+fragment.getName();
//			}
//		} 
  //save child interview
//		childInterviewVO = interviewService.create(childInterviewVO);		

  //save linking question
//		InterviewQuestionAnswerVO iqa = new InterviewQuestionAnswerVO();
//		iqa.setQuestion(questionVO);
//		iqa.setIdInterview(questionVO.getActiveInterviewId());

  //		if(childInterviewVO.getModule()!=null){
//			linkedModuleName = "Module:"+childInterviewVO.getModule().getIdNode()+":"+childInterviewVO.getModule().getName();
//		}
//		if(childInterviewVO.getFragment()!=null){
//			linkedModuleName = "Fragment:"+childInterviewVO.getFragment().getIdNode()+":"+childInterviewVO.getFragment().getName();
//		}
//		iqa.setInterviewQuestionAnswerFreetext("Linked "+linkedModuleName);
//		interviewVO.getQuestionsAsked().add(iqa);
//		interviewService.update(interviewVO);	
//		retValue = this.getNearestQuestion(childInterviewVO);
//		return retValue;
//	}
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
//		List<InterviewQuestionAnswerVO> questionsAsked = new ArrayList<InterviewQuestionAnswerVO>();
//		if(questionVO==null){//not found in child interviews
//			if(interviewVO.getQuestionsAsked().size()==0){//this is the first question in the module
//	    		if(interviewVO.getModule()!=null){
//	    			questionVO = interviewVO.getModule().getChildNodes().get(0);
//	    			questionVO.setActiveInterviewId(interviewVO.getInterviewId());
//				}else if(interviewVO.getFragment()!=null){
//					questionVO = interviewVO.getFragment().getChildNodes().get(0);
//					questionVO.setActiveInterviewId(interviewVO.getInterviewId());
//				} 		
//	    	}else{
//	    		questionsAsked = interviewVO.getQuestionsAsked();
//	    		Collections.sort(questionsAsked);  	
//	    		for(InterviewQuestionAnswerVO iqa: questionsAsked){
//	    			if(iqa.getPossibleAnswer()!=null){
//	    				List<QuestionVO> questions = iqa.getPossibleAnswer().getChildNodes();
//	    				for(QuestionVO question: questions){   
//	        				if(!isQuesitonAnswered(question,questionsAsked)){
//	        					if(question.compareTo(questionVO)<0){
//	        						questionVO = question;    
//	        						questionVO.setActiveInterviewId(interviewVO.getInterviewId());
//	        					}
//	        				}
//	        			}				
//	    			}
//	    		}
//	    	}
//		} 	
//    	if(questionVO==null){//if not found
    //then check root questions
//			if(interviewVO.getFragment()!=null){//if a aJSM check in root questions list
//				List<QuestionVO> childQuestions = interviewVO.getFragment().getChildNodes();
//				if(childQuestions.size()==0){					
//					for(FragmentVO fragment:fragmentService.findById(interviewVO.getFragment().getIdNode())){
//						childQuestions = fragment.getChildNodes();
//					}
//				}
//    			for(QuestionVO question: childQuestions){     				
//    				if(!isQuesitonAnswered(question,questionsAsked)){					
//    					questionVO = question;	
//    					questionVO.setActiveInterviewId(interviewVO.getInterviewId());
//    					break;
//    				}
//    			}
//    		}else { //must be a module check in module root questions list
//    			List<QuestionVO> childQuestions = interviewVO.getModule().getChildNodes();
//				if(childQuestions.size()==0){					
//					for(ModuleVO module:moduleService.findById(interviewVO.getModule().getIdNode())){
//						childQuestions = module.getChildNodes();
//					}
//				}
//    			for(QuestionVO question: childQuestions){     				
//    				if(!isQuesitonAnswered(question,questionsAsked)){
//    					questionVO = question;	
//    					questionVO.setActiveInterviewId(interviewVO.getInterviewId());
//    					break;
//    				}
//    			}
//    		}	
//    	}
//    	if(questionVO!=null){//if not null check if a linking question
//			if(questionVO.getLink()>0){//this is a linking question
//				questionVO = this.getNextInterviewQuestionFromLinkedInterview(questionVO,interviewVO);				
//			}
//		}
    return questionVO;
  }
//	private boolean isQuesitonAnswered(QuestionVO q,List<InterviewQuestionAnswerVO> questionsAsked){
//    	boolean retValue = false;
//    	for(InterviewQuestionAnswerVO iqa: questionsAsked){
//    		if(iqa.getQuestion().getIdNode()==q.getIdNode()){
//    			if((iqa.getDeleted()==0)){//not deleted
//    				retValue = true;
//    			}
//    		}
//    	}
//    	return retValue;
}
//}
