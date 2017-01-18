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
import org.occideas.entity.LanguageFragBreakdown;
import org.occideas.entity.LanguageModBreakdown;
import org.occideas.entity.NodeNodeLanguageFrag;
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
	
	@GET
	@Path(value="/getNodeNodeLanguageFragmentList")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getNodeNodeLanguageFragmentList(){
		List<NodeNodeLanguageFrag> list = null;
		try{
			list = service.getNodeNodeLanguageFragList();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@GET
	@Path(value="/getUntranslatedModules")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getUntranslatedModules(@QueryParam("flag") String flag){
		Integer result = null;
		try{
			result = service.getUntranslatedModules(flag);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(result).build();
	}

	@GET
	@Path(value="/getTotalUntranslatedModule")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getTotalUntranslatedModule(){
		Integer result = 0;
		try{
			result = service.getTotalUntranslatedModule();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(result).build();
	}
	
	@GET
	@Path(value="/getTotalModuleCount")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getTotalModuleCount(){
		Integer result = 0;
		try{
			result = service.getTotalModuleCount();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(result).build();
	}
	
	@GET
	@Path(value="/getUntranslatedFragments")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getUntranslatedFragments(@QueryParam("flag") String flag){
		Integer result = null;
		try{
			result = service.getUntranslatedFragments(flag);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(result).build();
	}

	@GET
	@Path(value="/getTotalUntranslatedFragment")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getTotalUntranslatedFragment(){
		Integer result = 0;
		try{
			result = service.getTotalUntranslatedFragment();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(result).build();
	}
	
	@GET
	@Path(value="/getTotalFragmentCount")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getTotalFragmentCount(){
		Integer result = 0;
		try{
			result = service.getTotalFragmentCount();
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(result).build();
	}
	
	@GET
	@Path(value="/getLanguageModBreakdown")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getLanguageModBreakdown(@QueryParam("flag") String flag){
		List<LanguageModBreakdown> result = new ArrayList<>();
		try{
			result = service.getLanguageModBreakdown(flag);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(result).build();
	}
	
	@GET
	@Path(value="/getLanguageFragBreakdown")
	@Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response getLanguageFragBreakdown(@QueryParam("flag") String flag){
		List<LanguageFragBreakdown> result = new ArrayList<>();
		try{
			result = service.getLanguageFragBreakdown(flag);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(result).build();
	}
	
	
}
