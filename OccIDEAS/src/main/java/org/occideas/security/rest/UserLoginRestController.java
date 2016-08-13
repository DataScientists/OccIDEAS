package org.occideas.security.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.occideas.exceptions.InvalidCurrentPasswordException;
import org.occideas.security.service.UserService;
import org.occideas.vo.PasswordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/user")
public class UserLoginRestController {

	@Autowired
	private UserService service;
	
	@Path(value = "/changePassword")
	@POST
	@Consumes(value = MediaType.APPLICATION_JSON_VALUE)
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response changePassword(PasswordVO vo) {
		try {
			service.changePassword(vo);
			return Response.ok().build();
		}catch(InvalidCurrentPasswordException e){
			e.printStackTrace();
			return Response.status(Status.EXPECTATION_FAILED).type("text/plain").entity(e.getMessage()).build();
		}
		catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
	}
	
}
