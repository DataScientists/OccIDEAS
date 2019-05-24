package org.occideas.jmx.rest;

import org.apache.commons.lang3.StringUtils;
import org.occideas.base.rest.BaseRestController;
import org.occideas.jmx.service.JMXServiceInterface;
import org.occideas.utilities.PropUtil;
import org.occideas.vo.JMXLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Path("/jmx")
public class JMXRestController implements BaseRestController<JMXLogVO> {
  @Autowired
  private JMXServiceInterface service;


  @GET
  @Path(value = "/list")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response list() {
    List<JMXLogVO> list = new ArrayList<>();
    try {
      list = service.list();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }


  @Path(value = "/exportJMeter")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public Response exportJMeter(List<JMXLogVO> list) throws IOException {
    String pathStr = service.createJMXFile(list);
    if (StringUtils.isEmpty(pathStr)) {
      return Response.status(Status.BAD_REQUEST).type("text/plain")
        .entity("The path to save the JMX file is not defined or invalid.").build();
    }
    java.nio.file.Path path = Paths.get(pathStr);
    return Response.ok(getOut(Files.readAllBytes(path), pathStr),
      javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM)
      .header("content-disposition", "attachment; filename = " + PropUtil.getInstance().getProperty("jmx.filename"))
      .build();
  }

  private StreamingOutput getOut(final byte[] excelBytes, String pathStr) {
    StreamingOutput fileStream = new StreamingOutput() {
      @Override
      public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
        try {
          java.nio.file.Path path = Paths.get(pathStr);
          byte[] data = Files.readAllBytes(path);
          output.write(data);
          output.flush();
        } catch (Exception e) {
          throw new WebApplicationException();
        }
      }
    };
    return fileStream;
  }


  @Override
  public Response listAll() {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public Response get(Long id) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public Response create(JMXLogVO json) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public Response update(JMXLogVO json) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public Response delete(JMXLogVO json) {
    // TODO Auto-generated method stub
    return null;
  }
}
