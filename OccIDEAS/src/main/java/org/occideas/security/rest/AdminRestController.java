package org.occideas.security.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.occideas.admin.service.IAdminService;
import org.occideas.security.service.UserService;
import org.occideas.vo.UserProfileVO;
import org.occideas.vo.UserUserProfileVO;
import org.occideas.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/admin")
public class AdminRestController {

    @Autowired
    private UserService service;

    @Autowired
    private IAdminService adminService;

    @GET
    @Path(value = "/purgeParticipants")
    public Response purgeParticipants()
    {
        try
        {
            adminService.purgeParticipants();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }
    
    
    @GET
    @Path(value = "/purgeModule")
    public Response purgeModule()
    {
        try
        {
            adminService.purgeModule();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }
    

    @GET
    @Path(value = "/cleanOrphans")
    public Response cleanOrphans()
    {
        try
        {
            adminService.cleanOrphans();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }


    @GET
    @Path(value = "/getUserRoles")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getUserRoles()
    {
        List<UserVO> list = new ArrayList<UserVO>();
        try
        {
            list = service.getUserRoles();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(list).build();
    }


    @GET
    @Path(value = "/getRoles")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getRoles()
    {
        List<UserProfileVO> list = new ArrayList<UserProfileVO>();
        try
        {
            list = service.getRoles();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok(list).build();
    }


    @Path(value = "/addUser")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response addUser(UserVO vo)
    {
        // check if user already exist before save
        if (service.findBySso(vo.getSsoId()) != null)
        {
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity("User already exist.").build();
        }
        try
        {
            UserVO userVO = service.save(vo);
            return Response.ok(userVO).build();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }


    @Path(value = "/updateUser")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response updateUser(UserVO vo)
    {
        try
        {
            UserVO userVO = service.update(vo);
            return Response.ok(userVO).build();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }


    @Path(value = "/updatePassword")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response updatePassword(UserVO vo)
    {
        try
        {
            UserVO userVO = service.save(vo);
            return Response.ok(userVO).build();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }


    @Path(value = "/saveUserProfile")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response saveUserProfile(UserUserProfileVO vo)
    {
        try
        {
            service.saveUserUserProfile(vo);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }


    @Path(value = "/saveUserProfileList")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response saveUserProfileList(List<UserUserProfileVO> vo)
    {
        try
        {
            service.saveUserUserProfileList(vo);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }


    @Path(value = "/deleteUserProfile")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response deleteUserProfile(int userId)
    {
        try
        {
            service.deleteUserUserProfile(userId);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }


}
