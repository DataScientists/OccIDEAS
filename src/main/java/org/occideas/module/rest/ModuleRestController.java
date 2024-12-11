package org.occideas.module.rest;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.occideas.base.rest.BaseRestController;
import org.occideas.entity.*;
import org.occideas.module.service.ModuleService;
import org.occideas.node.service.INodeService;
import org.occideas.qsf.service.IQSFService;
import org.occideas.security.handler.TokenManager;
import org.occideas.security.model.TokenResponse;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.utilities.MSWordGenerator;
import org.occideas.vo.*;
import org.occideas.voxco.service.IVoxcoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Path("/module")
public class ModuleRestController implements BaseRestController<ModuleVO> {

    @Autowired
    private ModuleService service;
    @Autowired
    private SystemPropertyService sysPropService;
    @Autowired
    private MSWordGenerator msWordGenerator;
    @Autowired
    private INodeService nodeService;
    @Autowired
    private IQSFService iqsfService;
    @Autowired
    private IVoxcoService voxcoService;

    private String FREE_TEXT_REGEX = "\\[free\\s?text\\]";

    private Pattern pattern = Pattern.compile(FREE_TEXT_REGEX, Pattern.CASE_INSENSITIVE);

    @GET
    @Path(value = "/getlist")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response listAll() {
        List<ModuleVO> list = new ArrayList<ModuleVO>();
        try {
            list = service.listAll();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(list).build();
    }

    @GET
    @Path(value = "/getJson")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getJson(@QueryParam("id") Long id) {
        ModuleVO modvo = new ModuleVO();
        SystemPropertyVO vo = null;
        if (id == -1) {
            vo = sysPropService.getByName(Constant.STUDY_INTRO);
            if (vo != null) {
                if (NumberUtils.isNumber(vo.getValue())) {
                    id = Long.valueOf(vo.getValue());
                } else {
                    return Response.status(Status.BAD_REQUEST).type("text/plain")
                            .entity("Verify that " + Constant.STUDY_INTRO + " in System Config is a number.").build();
                }
            }
        }
        if (id == -1 && vo == null) {
            return Response.status(Status.BAD_REQUEST).type("text/plain")
                    .entity("Unable to find " + Constant.STUDY_INTRO + " in System Config.").build();
        }

        try {
            modvo = service.getStudyAgentJSON(id);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(modvo).build();
    }

    @GET
    @Path(value = "/convertModuleToApplicationQSF")
    @Consumes(value = javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Produces(value = javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public Response convertModuleToApplicationQSF(@QueryParam("id") Long id,
                                                  @QueryParam("filter") boolean filterStudy) throws IOException {
        try {
            service.manualBuildQSF(id, filterStudy);
            //hack for entering all modules
        //    ArrayList<Long> allJobModuleIds = new ArrayList<Long>();
        //    allJobModuleIds.add(id);
          /*  allJobModuleIds.add(Long.valueOf(75434));
            allJobModuleIds.add(Long.valueOf(75433));
            allJobModuleIds.add(Long.valueOf(75432));
            allJobModuleIds.add(Long.valueOf(75443));
            allJobModuleIds.add(Long.valueOf(75441));
            allJobModuleIds.add(Long.valueOf(75440));
            allJobModuleIds.add(Long.valueOf(75816));
            allJobModuleIds.add(Long.valueOf(75811));
            allJobModuleIds.add(Long.valueOf(75438));
            allJobModuleIds.add(Long.valueOf(75437));
            allJobModuleIds.add(Long.valueOf(75436));
            allJobModuleIds.add(Long.valueOf(75435));*/
       //     allJobModuleIds.add(Long.valueOf(75439));
        //    allJobModuleIds.add(Long.valueOf(75442));
      //      for( Long moduleId: allJobModuleIds){
      //          service.manualBuildQSF(moduleId, filterStudy);
      //      }
            return Response.ok().build();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @GET
    @Path(value = "/exportQSFResponse")
    @Consumes(value = javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Produces(value = javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public Response exportQSFResponse(@QueryParam("id") Long id) throws IOException, InterruptedException {
        try {
            iqsfService.exportResponseQSF(id);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
        return Response.ok().build();
    }

    @GET
    @Path(value = "/exportToVoxco")
    @Consumes(value = javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Produces(value = javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public Response exportToVoxco(@QueryParam("id") Long id) {
        try {
            voxcoService.exportSurvey(id);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
        return Response.ok().build();
    }

    private String extractUserFromToken() {
        TokenManager tokenManager = new TokenManager();
        String token = ((TokenResponse) SecurityContextHolder.getContext().getAuthentication().getDetails()).getToken();
        return tokenManager.parseUsernameFromToken(token);
    }

    @GET
    @Path(value = "/get")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("id") Long id) {
        List<ModuleVO> list = new ArrayList<ModuleVO>();
        SystemPropertyVO vo = null;
        if (id == -1) {
            vo = sysPropService.getByName(Constant.STUDY_INTRO);
            if (vo != null) {
                if (NumberUtils.isNumber(vo.getValue())) {
                    id = Long.valueOf(vo.getValue());
                } else {
                    return Response.status(Status.BAD_REQUEST).type("text/plain")
                            .entity("Verify that " + Constant.STUDY_INTRO + " in System Config is a number.").build();
                }
            }
        }
        if (id == -1 && vo == null) {
            return Response.status(Status.BAD_REQUEST).type("text/plain")
                    .entity("Unable to find " + Constant.STUDY_INTRO + " in System Config.").build();
        }

        try {
            list = service.findById(id);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(list).build();
    }

    @GET
    @Path(value = "/getModuleById")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getModuleById(@QueryParam("id") Long id) {
        ModuleVO module = null;
        try {
            module = nodeService.getModule(id);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(module).build();
    }

    @GET
    @Path(value = "/getNodeById")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getNodeById(@QueryParam("id") Long id) {
        NodeVO nodeVO = null;
        try {
            nodeVO = nodeService.getNode(id);
            if (nodeVO instanceof ModuleVO) {
                ModuleVO vo = (ModuleVO) nodeVO;
                return Response.ok(vo).build();
            }
            if (nodeVO instanceof QuestionVO) {
                QuestionVO vo = (QuestionVO) nodeVO;
                return Response.ok(vo).build();
            }
            if (nodeVO instanceof PossibleAnswerVO) {
                PossibleAnswerVO vo = (PossibleAnswerVO) nodeVO;
                return Response.ok(vo).build();
            }
            return Response.ok().build();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Path(value = "/exportToWord")
    @GET
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_OCTET_STREAM)
    public Response exportToWord(@QueryParam("idNode") String idNode,
                                 @DefaultValue("false") @QueryParam("filterStudyAgent") String filterStudyAgent) {
        boolean shouldFilter = "true".equals(filterStudyAgent);
        if (idNode == null || idNode.equals(0L)) {
            return Response.status(Status.BAD_REQUEST).type("text/plain")
                    .entity("export to Word , invalid data submitted for this request.").build();
        }
        try {
            File file = msWordGenerator.writeDocument(idNode, shouldFilter);
            return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"").build();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    private void generateNumbersAndDescriptionsToString(NodeVO vo, List<String> numbers, List<String> descriptions) {
        String questionSpace = " ";
        String answerSpace = "   ";
        String name = vo.getName();
        if (vo instanceof QuestionVO) {
            name = questionSpace + name;
        } else if (vo instanceof PossibleAnswerVO) {
            name = answerSpace + name;
        }
        numbers.add(vo.getNumber());
        descriptions.add(name);
        if (vo.getChildNodes() != null && !vo.getChildNodes().isEmpty()) {
            for (NodeVO v : vo.getChildNodes()) {
                generateNumbersAndDescriptionsToString(v, numbers, descriptions);
            }
        }
    }

    @GET
    @Path(value = "/getModuleFilterStudyAgent")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getModuleFilterStudyAgent(@QueryParam("id") Long id) {
        NodeVO vo = null;
        try {
            vo = service.getModuleFilterStudyAgent(id);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        List<NodeVO> list = new ArrayList<>();
        list.add(vo);
        return Response.ok(list).build();
    }

    @GET
    @Path(value = "/getModuleFilterAgent")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getModuleFilterAgent(@QueryParam("id") Long id, @QueryParam("idAgent") Long idAgent) {
        NodeVO vo = null;
        try {
            vo = service.getModuleFilterAgent(id, idAgent);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        List<NodeVO> list = new ArrayList<>();
        list.add(vo);
        return Response.ok(list).build();
    }

    @GET
    @Path(value = "/getinterviewmodule")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getInterviewModule(@QueryParam("id") Long id) {
        List<ModuleVO> list = new ArrayList<ModuleVO>();
        boolean isIntroModule = false;
        try {
            SystemPropertyVO vo = null;
            if (id == -1) {
                vo = sysPropService.getByName(Constant.STUDY_INTRO);
                if (vo != null) {
                    if (NumberUtils.isNumber(vo.getValue())) {
                        id = Long.valueOf(vo.getValue());
                    } else {
                        return Response.status(Status.BAD_REQUEST).type("text/plain")
                                .entity("Verify that " + Constant.STUDY_INTRO + " in System Config is a number.")
                                .build();
                    }
                }
            }
            if (id == -1 && vo == null) {
                return Response.status(Status.BAD_REQUEST).type("text/plain")
                        .entity("Unable to find " + Constant.STUDY_INTRO + " in System Config.").build();
            }
            list = service.findByIdForInterview(id);
            if (isIntroModule && list.get(0) == null) {
                return Response.status(Status.BAD_REQUEST).type("text/plain")
                        .entity("Study intro module does not exist:" + id).build();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(list).build();
    }

    @Path(value = "/create")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response create(ModuleVO json) {
        try {
            service.create(json);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @Path(value = "/setActiveIntroModule")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response setActiveIntroModule(ModuleVO vo) {
        try {
            service.setActiveIntroModule(vo);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @Path(value = "/update")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response update(ModuleVO json) {
        Long idNode = null;
        try {
            idNode = service.update(json);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(idNode).build();
    }

    public Response save(ModuleVO json) {
        Long idNode = null;
        try {
            idNode = service.save(json);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(idNode).build();
    }

    @Path(value = "/delete")
    @POST
    public Response delete(ModuleVO json) {
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
    public Response saveCopy(ModuleCopyVO json) {
        NodeRuleHolder idNodeHolder = null;
        try {
            idNodeHolder = service.copyModule(json);
            if (json.isIncludeRules()) {
                service.copyRules(idNodeHolder);
                service.addNodeRules(idNodeHolder);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(idNodeHolder.getIdNode()).build();
    }

    @Path(value = "/importJson")
    @POST
    public Response importJson(FormDataMultiPart multiPart) {
        ModuleReportVO report = new ModuleReportVO();
        try {
            Map<String, List<FormDataBodyPart>> fieldsByName = multiPart.getFields();

            for (List<FormDataBodyPart> fields : fieldsByName.values()) {
                for (FormDataBodyPart field : fields) {
                    InputStream in = field.getEntityAs(InputStream.class);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                    NodeRuleHolder idNodeHolder = null;
                    String line = reader.readLine();
                    ModuleVO[] modules = mapper.readValue(line, ModuleVO[].class);
                    // only expecting one module
                    ModuleVO vo = modules[0];
                    ModuleCopyVO copyVo = new ModuleCopyVO();
                    copyVo.setVo(vo);
                    copyVo.setFragments(vo.getFragments());
                    copyVo.setIncludeRules(true);
                    copyVo.setName(vo.getName());
                    copyVo.setModules(vo.getModules());
                    // this is for intro module
                    if ("M_IntroModule".equals(vo.getType())) {
                        idNodeHolder = service.copyModuleAutoGenerateModule(copyVo, report);
                        service.updateMissingLinks(copyVo.getVo());
                        for (ModuleVO moduleVO : copyVo.getModules()) {
                            service.updateMissingLinks(moduleVO);
                        }
                    } else {
                        // this is for fragment
                        idNodeHolder = service.copyModuleAutoGenerateFragments(copyVo, report);
                        service.updateMissingLinks(copyVo.getVo());
                        for (FragmentVO fragmentVO : copyVo.getFragments()) {
                            service.updateMissingLinks(fragmentVO);
                        }
                    }
                    // missing rules report
                    service.copyRulesValidateAgent(idNodeHolder, report);
                    service.addNodeRulesValidateAgent(idNodeHolder, report);
                    // adding module vo to the report
                    report.setVo(vo);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }

        return Response.ok(report).build();
    }

    @GET
    @Path(value = "/getAllModulesReport")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getAllModulesReport() {
        List<ModuleReportVO> report = new ArrayList<>();

        List<JobModule> modules;
        try {
            modules = service.getAllModules();
            report = generateReport(modules);

        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(report).build();
    }

    @GET
    @Path(value = "/getNodeNameById")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getNodeNameById(@QueryParam("id") Long idNode) {
        NodeVO nodeVO = null;
        try {
            nodeVO = service.getNodeById(idNode);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(nodeVO).build();
    }

    @GET
    @Path(value = "/getTotalUntranslatedModule")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getTotalUntranslatedModule(@QueryParam("languageId") long languageId) {
        try {
            Integer total = service.getTotalUntranslatedModule(languageId);
            return Response.ok(total).build();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @GET
    @Path(value = "/getTotalTranslatedNodeByLanguage")
    @Produces(value = MediaType.APPLICATION_JSON)
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
    @Path(value = "/getModulesWithTranslationCount")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getModulesWithTranslationCount(@QueryParam("languageId") long languageId) {
        try {
            Integer total = service.getModulesWithTranslationCount(languageId);
            return Response.ok(total).build();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @GET
    @Path(value = "/getModuleTranslationTotalCount")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getModuleTranslationTotalCount(@QueryParam("id") String idNode) {
        try {
            Integer total = service.getModuleTranslationTotalCount(idNode);
            return Response.ok(total).build();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @GET
    @Path(value = "/getModuleTranslationCurrentCount")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getModuleTranslationCurrentCount(@QueryParam("id") String idNode,
                                                     @QueryParam("languageId") long languageId) {
        try {
            Integer total = service.getModuleTranslationCurrentCount(idNode, languageId);
            return Response.ok(total).build();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @GET
    @Path(value = "/getModuleLanguageBreakdown")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getModuleLanguageBreakdown(@QueryParam("languageId") long languageId) {
        try {
            List<LanguageModBreakdownVO> vo = service.getModuleLanguageBreakdown(languageId);
            return Response.ok(vo).build();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    private List<ModuleReportVO> generateReport(List<JobModule> modules) {
        List<ModuleReportVO> reports = new ArrayList();

        for (JobModule module : modules) {

            ModuleReportVO report = new ModuleReportVO();
            ModuleVO vo = new ModuleVO();
            vo.setName(module.getName());
            vo.setIdNode(module.getIdNode());
            report.setVo(vo);
            populateQuestions(module.getChildNodes(), report);
            reports.add(report);
        }

        return reports;
    }

	private void populateQuestions(List<Question> childNodes, ModuleReportVO report) {

		for (Question vo : childNodes) {
			report.setTotalQuestions(report.getTotalQuestions() + 1);
			if (!vo.getChildNodes().isEmpty()) {
				populateAnswers(vo.getChildNodes(), report);
			} else {
				if (vo.getDescription() != "display") {
					report.addIssue(vo.getNumber() + " " + vo.getName());
				}
			}
		}
	}

    private void populateAnswers(List<PossibleAnswer> childNodes, ModuleReportVO report) {

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

    private void populateRules(List<ModuleRule> moduleRule, ModuleReportVO report) {
        report.setTotalRules(moduleRule.size() + report.getTotalRules());
    }
}
