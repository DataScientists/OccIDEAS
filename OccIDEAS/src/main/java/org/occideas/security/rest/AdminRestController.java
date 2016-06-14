package org.occideas.security.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.occideas.security.service.UserService;
import org.occideas.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/admin")
public class AdminRestController {

	@Autowired
	private UserService service;
	
	@GET
    @Path(value = "/getUserRoles")
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response getUserRoles() {
    	List<UserVO> list = new ArrayList<UserVO>();
		try{
			list = service.getUserRoles();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
    }
	
}
