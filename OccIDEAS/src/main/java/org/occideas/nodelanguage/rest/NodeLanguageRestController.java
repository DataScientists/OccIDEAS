package org.occideas.nodelanguage.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.occideas.base.rest.BaseRestController;
import org.occideas.entity.NodeNodeLanguageMod;
import org.occideas.nodelanguage.service.NodeLanguageService;
import org.occideas.vo.LanguageVO;
import org.occideas.vo.NodeLanguageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/nodelanguage")
public class NodeLanguageRestController implements BaseRestController<NodeLanguageVO>{

	@Autowired
	private NodeLanguageService service;	
	
	@GET
	@Path(value="/getNodeByLanguage")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getNodeByLanguage(@QueryParam("language") String language) {
		List<NodeLanguageVO> list = new ArrayList<>();
		try{
			list = service.getNodesByLanguage(language);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@GET
	@Path(value="/getNodeLanguageById")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getNodeLanguageById(@QueryParam("id") String id) {
		List<NodeLanguageVO> list = new ArrayList<>();
		try{
			list = service.getNodeLanguageById(id);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@Override
	public Response get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Path(value = "/save")
	@POST
	@Consumes(value = MediaType.APPLICATION_JSON_VALUE)
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response create(NodeLanguageVO vo) {
		try{
			service.save(vo);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}
	
	@Path(value = "/addLanguage")
	@POST
	@Consumes(value = MediaType.APPLICATION_JSON_VALUE)
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response addLanguage(LanguageVO vo){
		try{
			service.addLanguage(vo);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}
	
	@GET
	@Path(value="/getAllLanguage")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getAllLanguage(){
		List<LanguageVO> list = new ArrayList<>();
		try{
			list = service.getAllLanguage();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@GET
	@Path(value="/getDistinctLanguage")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getDistinctLanguage(){
		List<LanguageVO> list = new ArrayList<>();
		try{
			list = service.getDistinctLanguage();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@Path(value = "/getNodesByLanguageAndWord")
	@POST
	@Consumes(value = MediaType.APPLICATION_JSON_VALUE)
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getNodesByLanguageAndWord(NodeLanguageVO vo){
		NodeLanguageVO nodeLanguage = null;
		try{
			nodeLanguage = service.getNodesByLanguageAndWord(vo.getLanguageId(), vo.getWord());
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(nodeLanguage).build();
	}
	

	@Override
	public Response update(NodeLanguageVO json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Path(value = "/delete")
	@POST
	public Response delete(NodeLanguageVO vo) {
		try {
			service.delete(vo);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@Override
	public Response listAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path(value="/getLanguageById")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getLanguageById(@QueryParam("id") String id) {
		LanguageVO vo = null;
		try{
			vo = service.getLanguageById(Long.valueOf(id));
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(vo).build();
	}
	
	@GET
	@Path(value="/getNodeNodeLanguageList")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getNodeNodeLanguageList(){
		List<NodeNodeLanguageMod> list = null;
		try{
			list = service.getNodeNodeLanguageListMod();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
}
