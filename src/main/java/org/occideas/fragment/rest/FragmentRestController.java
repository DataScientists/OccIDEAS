package org.occideas.fragment.rest;

import org.occideas.base.rest.BaseRestController;
import org.occideas.entity.Fragment;
import org.occideas.entity.ModuleRule;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.fragment.service.FragmentService;
import org.occideas.module.service.ModuleService;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Path("/fragment")
public class FragmentRestController implements BaseRestController<FragmentVO> {

  private String FREE_TEXT_REGEX = "\\[free\\s?text\\]";

  private Pattern pattern = Pattern.compile(FREE_TEXT_REGEX, Pattern.CASE_INSENSITIVE);

  @Autowired
  private FragmentService service;

  @Autowired
  private ModuleService moduleService;

  @GET
  @Path(value = "/getlist")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response listAll() {
    List<FragmentVO> list = new ArrayList<FragmentVO>();
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
    List<FragmentVO> list = new ArrayList<FragmentVO>();
    try {
      list = service.findById(id);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/getFilterStudyAgents")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getFilterStudyAgents(@QueryParam("id") Long id) {
    List<FragmentVO> list = new ArrayList<FragmentVO>();
    try {
      list = service.getFilterStudyAgents(id);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/getFilterAgents")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getFilterAgents(@QueryParam("id") Long id, @QueryParam("idAgent") Long idAgent) {
    List<FragmentVO> list = new ArrayList<FragmentVO>();
    try {
      list = service.getFilterStudyAgents(id, idAgent);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/getLinkingNodes")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getLinkingNodes(@QueryParam("id") Long id) {
    List<QuestionVO> list = new ArrayList<QuestionVO>();
    try {
      list = service.getLinkingNodes(id);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/checkexists")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response checkExists(@QueryParam("id") Long id) {
    boolean bExists = false;
    try {
      bExists = service.checkExists(id);
    } catch (Throwable e) {
      bExists = false;
    }
    return Response.ok(bExists).build();
  }

  @Path(value = "/create")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response create(FragmentVO json) {
    try {
      service.createFragment(json);
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
  public Response update(FragmentVO json) {
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
  public Response delete(FragmentVO json) {
    try {
      service.merge(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }

  @Path(value = "/saveAs")
  @POST
  public Response saveCopy(FragmentCopyVO json) {
    NodeRuleHolder idNodeHolder = null;
    try {
      FragmentReportVO vo = new FragmentReportVO();
      idNodeHolder = service.copyFragment(json, vo);
      if (json.isIncludeRules()) {
        moduleService.copyRules(idNodeHolder);
        moduleService.addNodeRules(idNodeHolder);
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(idNodeHolder.getIdNode()).build();
  }

  @GET
  @Path(value = "/getAllFragmentsReport")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getAllFragmentsReport() {
    List<FragmentReportVO> report = new ArrayList<>();

    List<Fragment> fragments;
    try {
      fragments = service.getAllFragments();
      report = generateReport(fragments);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(report).build();
  }

  private List<FragmentReportVO> generateReport(List<Fragment> fragments) {
    List<FragmentReportVO> reports = new ArrayList();

    for (Fragment fragment : fragments) {

      FragmentReportVO report = new FragmentReportVO();
      FragmentVO vo = new FragmentVO();
      vo.setName(fragment.getName());
      vo.setIdNode(fragment.getIdNode());
      report.setVo(vo);
      populateQuestions(fragment.getChildNodes(), report);
      reports.add(report);
    }

    return reports;
  }

  private void populateQuestions(List<Question> childNodes,
                                 FragmentReportVO report) {

    for (Question vo : childNodes) {
      report.setTotalQuestions(report.getTotalQuestions() + 1);
      if (!vo.getChildNodes().isEmpty()) {
        populateAnswers(vo.getChildNodes(), report);
      }else{
    	if(vo.getDescription()!="display"){
          report.addIssue(vo.getNumber() + " " + vo.getName());
    	} 	  
      }
    }
  }

  private void populateAnswers(List<PossibleAnswer> childNodes,
                               FragmentReportVO report) {

    for (PossibleAnswer vo : childNodes) {
      report.setTotalAnswers(report.getTotalAnswers() + 1);
      if (vo.getType().equals("P_freetext") && !pattern.matcher(vo.getName()).find()) {
        report.addIssue(vo.getNumber() + " " + vo.getName());
      }

      if (!vo.getModuleRule().isEmpty()) {
        populateRules(vo.getModuleRule(), report);
      }
      if (!vo.getChildNodes().isEmpty()) {
        populateQuestions(vo.getChildNodes(), report);
      }
    }
  }

  private void populateRules(List<ModuleRule> moduleRule, FragmentReportVO report) {
    report.setTotalRules(moduleRule.size() + report.getTotalRules());
  }

  /**
   * Get the fragment's parent link
   *
   * @param id
   * @return
   */
  @GET
  @Path(value = "/getFragmentParents")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getFragmentParents(@QueryParam("id") Long id) {

    List<FragmentVO> fragments;
    try {
      fragments = service.getFragmentParents(id);

    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(fragments).build();
  }

  @GET
  @Path(value = "/getTotalUntranslatedFragment")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getTotalUntranslatedFragment(@QueryParam("languageId") long languageId) {
    try {
      Integer total = service.getTotalUntranslatedFragment(languageId);
      return Response.ok(total).build();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
  }

  @GET
  @Path(value = "/getTotalTranslatedNodeByLanguage")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getTotalTranslatedNodeByLanguage(@QueryParam("languageId") long languageId) {
    try {
      Integer total = service.getTotalTranslatedNodeByLanguage(languageId);
      return Response.ok(total).build();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
  }

  @GET
  @Path(value = "/getFragmentsWithTranslationCount")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getFragmentsWithTranslationCount(@QueryParam("languageId") long languageId) {
    try {
      Integer total = service.getFragmentWithTranslationCount(languageId);
      return Response.ok(total).build();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
  }

  @GET
  @Path(value = "/getFragmentTranslationTotalCount")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getFragmentTranslationTotalCount(@QueryParam("id") String idNode) {
    try {
      Integer total = service.getFragmentTranslationTotalCount(idNode);
      return Response.ok(total).build();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
  }

  @GET
  @Path(value = "/getFragmentTranslationCurrentCount")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getFragmentTranslationCurrentCount(@QueryParam("id") String idNode, @QueryParam("languageId") long languageId) {
    try {
      Integer total = service.getFragmentTranslationCurrentCount(idNode, languageId);
      return Response.ok(total).build();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
  }

  @GET
  @Path(value = "/getFragmentLanguageBreakdown")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getFragmentLanguageBreakdown(@QueryParam("languageId") long languageId) {
    try {
      List<LanguageFragmentBreakdownVO> vo = service.getFragmentLanguageBreakdown(languageId);
      return Response.ok(vo).build();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
  }
}
