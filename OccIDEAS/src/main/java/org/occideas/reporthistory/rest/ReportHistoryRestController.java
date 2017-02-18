package org.occideas.reporthistory.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.occideas.reporthistory.service.ReportHistoryService;
import org.occideas.vo.ReportHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/reportHistory")
public class ReportHistoryRestController {

	@Autowired
	private ReportHistoryService service;
	
	@Path(value="/downloadReport")
	@POST
    @Consumes(value=MediaType.APPLICATION_JSON_VALUE)
	@Produces({ "application/csv"})
	public Response downloadReport(ReportHistoryVO vo) throws IOException {
		java.nio.file.Path path = Paths.get(vo.getPath());
		return Response.ok(getOut(Files.readAllBytes(path),vo.getPath()), 
				javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM)
				.header("content-disposition","attachment; filename = "+vo.getName())
                .build();
	}
	
	private StreamingOutput getOut(final byte[] excelBytes,String pathStr) {
		 StreamingOutput fileStream =  new StreamingOutput() 
	        {
	            @Override
	            public void write(java.io.OutputStream output) throws IOException, WebApplicationException 
	            {
	                try
	                {
	                    java.nio.file.Path path = Paths.get(pathStr);
	                    byte[] data = Files.readAllBytes(path);
	                    output.write(data);
	                    output.flush();
	                } 
	                catch (Exception e) 
	                {
	                    throw new WebApplicationException("File Not Found !!");
	                }
	            }
	        };
	        return fileStream;
	}
	
	@GET
	@Path(value="/getAll")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getAll() {
		List<ReportHistoryVO> list = new ArrayList<>();
		try{
			list = service.getAll();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@GET
	@Path(value="/getByType")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getByType(@QueryParam("type") String type) {
		List<ReportHistoryVO> list = new ArrayList<>();
		try{
			list = service.getByType(type);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@Path(value="/save")
	@POST
    @Consumes(value=MediaType.APPLICATION_JSON_VALUE)
    @Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response save(ReportHistoryVO vo) {
		try{
			service.save(vo);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}
	
	@Path(value="/delete")
	@POST
    @Consumes(value=MediaType.APPLICATION_JSON_VALUE)
    @Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response delete(ReportHistoryVO vo) {
		try{
			service.delete(vo);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}
	
	@Path(value="/downloadLookup")
	@POST
    @Consumes(value=MediaType.APPLICATION_JSON_VALUE)
	@Produces({ "application/csv"})
	public Response downloadLookup(ReportHistoryVO vo) throws IOException {
		
		String pathStr = vo.getPath().substring(0, vo.getPath().length()-4).concat("-Lookup.csv");
		//String newName = vo.getName().substring(0, vo.getName().length()-4).concat("-Lookup.csv");
		java.nio.file.Path path = Paths.get(pathStr);
		if(path.toFile().exists()){
			return Response.ok(getOut(Files.readAllBytes(path),pathStr), 
					javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM)
					.header("content-disposition","attachment; filename = "+vo.getName())
	                .build();
		}
		
		return Response.status(Status.NO_CONTENT).type("text/plain").build();
	}
	
}
