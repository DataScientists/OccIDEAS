package org.occideas.anzscocoder.rest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.anzscocoder.service.AnzscoCoderService;
import org.occideas.vo.AnzscoLookupRequestVO;
import org.occideas.vo.AnzscoLookupResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/anzscocoder")
public class AnzscoCoderRestController {

    private static final Logger log = LogManager.getLogger(AnzscoCoderRestController.class);

    @Autowired
    private AnzscoCoderService anzscoCoderService;

    @POST
    @Path(value = "/lookup")
    @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_VALUE)
    public Response lookup(AnzscoLookupRequestVO request) {
        if (request == null || StringUtils.isBlank(request.getJobTitle())) {
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain")
                .entity("jobTitle is required").build();
        }
        try {
            AnzscoLookupResultVO result = anzscoCoderService.lookup(request.getJobTitle(),
                request.getJobDescription());
            return Response.ok(result).build();
        } catch (IllegalStateException e) {
            log.warn("ANZSCO lookup rejected: {}", e.getMessage());
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).type("text/plain")
                .entity(e.getMessage()).build();
        } catch (Throwable e) {
            log.error("Failed to look up ANZSCO code for jobTitle={}", request.getJobTitle(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain")
                .entity("Failed to look up ANZSCO code").build();
        }
    }
}
