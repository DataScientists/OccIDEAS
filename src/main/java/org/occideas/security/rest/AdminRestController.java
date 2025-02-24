package org.occideas.security.rest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.occideas.admin.service.IAdminService;
import org.occideas.admin.service.IDbConnectService;
import org.occideas.entity.NodePlain;
import org.occideas.ipsos.service.IIPSOSService;
import org.occideas.module.service.ModuleService;
import org.occideas.participant.service.ParticipantService;
import org.occideas.qsf.IQSFClient;
import org.occideas.qsf.QSFClient;
import org.occideas.qsf.response.Element;
import org.occideas.qsf.response.SurveyListResponse;
import org.occideas.qsf.service.IQSFService;
import org.occideas.security.service.UserService;
import org.occideas.vo.*;
import org.occideas.voxco.service.IVoxcoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.*;
import java.util.*;

@Path("/admin")
public class AdminRestController {

    @Autowired
    private UserService service;

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IDbConnectService iDbConnectService;

    @Autowired
    private IQSFService iqsfService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private IVoxcoService iVoxcoService;

    @Autowired
    private IIPSOSService iIPSOSService;


    @GET
    @Path(value = "/qsf/sync/{surveyId}")
    public Response syncQSFSurveyResponse(@PathParam("surveyId") String surveyId,@PathParam("reference") String reference) {
        try {
            iqsfService.importResponseQSF(surveyId,reference);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path(value = "/purgeParticipants")
    public Response purgeParticipants() {
        try {
            adminService.purgeParticipants();
            importJsons();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path(value = "/deleteQSFSurveys")
    public Response deleteQSFSurveys() {
        try {
            IQSFClient iqsfClient = new QSFClient();
            Response response = iqsfClient.listSurvey();
            if (response.getEntity() != null) {
                SurveyListResponse surveyListResponse = (SurveyListResponse) response.getEntity();
                int i = 0;
                int iSize = surveyListResponse.getResult().getElements().size();
                for (Element element : surveyListResponse.getResult().getElements()) {
                	System.out.println(i+ " of " + iSize);
                	Calendar myCalendar = new GregorianCalendar(2021, 9, 11);
                	Date myDate = myCalendar.getTime();
                	Calendar mySurveyCalendar = new GregorianCalendar();
                	mySurveyCalendar.setTime(element.getCreationDate());
                	mySurveyCalendar.set(Calendar.HOUR_OF_DAY, 0);
                	mySurveyCalendar.set(Calendar.MINUTE, 0);
                	mySurveyCalendar.set(Calendar.SECOND, 0);
                	mySurveyCalendar.set(Calendar.MILLISECOND, 0);
                	if(mySurveyCalendar.compareTo(myCalendar)==0) {
                		System.out.println("Not deleting "+element.getName());
                	}else {
                		System.out.println("Will delete"+element.getName());
                        iqsfClient.deleteSurvey(element.getId());
                	}
             
                    i++;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path(value = "/importQSFResponses")
    public Response importQSFResponses() {
        try {
            participantService.softDeleteAll();
            iqsfService.cleanSurveyResponses();
            iqsfService.importQSFResponses();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path(value = "/createQSFTranslationModule")
    @Produces(value = javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public Response createQSFTranslationModule() throws IOException {
        try {
            iqsfService.createQSFTranslationModule();
            return Response.ok().build();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @GET
    @Path(value = "/importVoxcoResponse")
    public Response importVoxcoResponse() {
        try {
            iVoxcoService.importVoxcoResponse(true);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path(value = "/exportAllToVoxco")
    public Response exportAllToVoxco() {
        try {
            iVoxcoService.exportAllToVoxco();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path(value = "/validateVoxcoQuestions")
    public Response validateVoxcoQuestions() {
        List<NodeVoxcoVO> active = new ArrayList<>();
        try {
            active = iVoxcoService.validateVoxcoQuestions();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(active).build();
    }

    @GET
    @Path(value = "/importIPSOSResponse")
    public Response importIPSOSResponse() {
        try {
            iIPSOSService.importResponse();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path(value = "/generateIPSOSJobModuleDataFile")
    public Response generateIPSOSJobModuleDataFile() {
        try {
            iIPSOSService.generateIPSOSJobModuleDataFile();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }


    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    @Path(value = "/copySurveys")
    public Response copySurveys(CopySurveyRequestVO request) {
        try {
            iqsfService.copySurveys(request.getUserId(),request.getPrefix());
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }


    @GET
    @Path(value = "/purgeModule")
    public Response purgeModule() {
        try {
            adminService.purgeModule();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }


    @GET
    @Path(value = "/cleanOrphans")
    public Response cleanOrphans() {
        try {
            adminService.cleanOrphans();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }


    @GET
    @Path(value = "/getUserRoles")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getUserRoles() {
        List<UserVO> list = new ArrayList<UserVO>();
        try {
            list = service.getUserRoles();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(list).build();
    }


    @GET
    @Path(value = "/getRoles")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getRoles() {
        List<UserProfileVO> list = new ArrayList<UserProfileVO>();
        try {
            list = service.getRoles();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(list).build();
    }


    @Path(value = "/addUser")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response addUser(UserVO vo) {
        // check if user already exist before save
        if (service.findBySso(vo.getSsoId()) != null) {
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity("User already exist.").build();
        }
        try {
            UserVO userVO = service.save(vo);
            return Response.ok(userVO).build();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Path(value = "/importLibrary")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response importLibrary(DBConnectVO vo) {
        try {
            List<NodePlain> importLibrary = iDbConnectService.importLibrary(vo);
            return Response.ok(importLibrary).build();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }


    @Path(value = "/updateUser")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response updateUser(UserVO vo) {
        try {
            UserVO userVO = service.update(vo);
            return Response.ok(userVO).build();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }


    @Path(value = "/updatePassword")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response updatePassword(UserVO vo) {
        try {
            UserVO userVO = service.save(vo);
            return Response.ok(userVO).build();
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }


    @Path(value = "/saveUserProfile")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response saveUserProfile(UserUserProfileVO vo) {
        try {
            service.saveUserUserProfile(vo);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }


    @Path(value = "/saveUserProfileList")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response saveUserProfileList(List<UserUserProfileVO> vo) {
        try {
            service.saveUserUserProfileList(vo);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }


    @Path(value = "/deleteUserProfile")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response deleteUserProfile(int userId) {
        try {
            service.deleteUserUserProfile(userId);
        } catch (Throwable e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }


    private Response importJsons() {
        ModuleReportVO report = new ModuleReportVO();
        try {
            //Map<String, List<FormDataBodyPart>> fieldsByName = multiPart.getFields();

            //for (List<FormDataBodyPart> fields : fieldsByName.values()) {
            //    for (FormDataBodyPart field : fields) {
            File file = new File("/opt/data/importJSONs/aNAB.json");
            InputStream in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
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
                idNodeHolder = moduleService.copyModuleAutoGenerateModule(copyVo, report);
                moduleService.updateMissingLinks(copyVo.getVo());
                for (ModuleVO moduleVO : copyVo.getModules()) {
                    moduleService.updateMissingLinks(moduleVO);
                }
            } else {
                // this is for fragment
                idNodeHolder = moduleService.copyModuleAutoGenerateFragments(copyVo, report);
                moduleService.updateMissingLinks(copyVo.getVo());
                for (FragmentVO fragmentVO : copyVo.getFragments()) {
                    moduleService.updateMissingLinks(fragmentVO);
                }
            }
            // missing rules report
            moduleService.copyRulesValidateAgent(idNodeHolder, report);
            moduleService.addNodeRulesValidateAgent(idNodeHolder, report);
            // adding module vo to the report
            report.setVo(vo);

            //    }
            //}
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }

        return Response.ok(report).build();
    }
}
