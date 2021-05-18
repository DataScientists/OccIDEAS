package org.occideas.security.rest;

import org.occideas.admin.service.IAdminService;
import org.occideas.admin.service.IDbConnectService;
import org.occideas.entity.NodePlain;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private IVoxcoService iVoxcoService;

    @GET
    @Path(value = "/purgeParticipants")
    public Response purgeParticipants() {
        try {
            adminService.purgeParticipants();
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
                    iqsfClient.deleteSurvey(element.getId());
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


}
