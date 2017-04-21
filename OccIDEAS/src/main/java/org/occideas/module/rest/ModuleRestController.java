package org.occideas.module.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.math.NumberUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.occideas.base.rest.BaseRestController;
import org.occideas.entity.Constant;
import org.occideas.entity.Module;
import org.occideas.entity.ModuleRule;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.module.service.ModuleService;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleCopyVO;
import org.occideas.vo.ModuleReportVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeRuleHolder;
import org.occideas.vo.NodeVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/module")
public class ModuleRestController implements BaseRestController<ModuleVO> {

	@Autowired
	private ModuleService service;
	@Autowired
	private SystemPropertyService sysPropService;
	
	private String FREE_TEXT_REGEX = "\\[free\\s?text\\]";
	
	private Pattern pattern = Pattern.compile(FREE_TEXT_REGEX, Pattern.CASE_INSENSITIVE);
		
	@GET
	@Path(value = "/getlist")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response listAll() {
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		try {
			list = service.listAll();
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}

	@GET
	@Path(value = "/get")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response get(@QueryParam("id") Long id) {
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		SystemPropertyVO vo = null;
		if (id == -1) {
			vo = sysPropService.getByName(Constant.STUDY_INTRO);
			if(vo !=null){
				if(NumberUtils.isNumber(vo.getValue())){
					id = Long.valueOf(vo.getValue());
				}else{
					return Response.status(Status.EXPECTATION_FAILED).type("text/plain").
							entity("Verify that "+Constant.STUDY_INTRO +" in System Config is a number.").build();
				}
			}
		}
		if(id == -1 && vo == null){
			return Response.status(Status.EXPECTATION_FAILED).type("text/plain").
					entity("Unable to find "+Constant.STUDY_INTRO +" in System Config.").build();
		}
		
		try {
			list = service.findById(id);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@GET
	@Path(value = "/getModuleFilterStudyAgent")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getModuleFilterStudyAgent(@QueryParam("id") Long id) {
		NodeVO vo = null;
		try {
			vo = service.getModuleFilterStudyAgent(id);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		List<NodeVO> list = new ArrayList<>();
		list.add(vo);
		return Response.ok(list).build();
	}
	
	@GET
	@Path(value = "/getModuleFilterAgent")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getModuleFilterAgent(@QueryParam("id") Long id,@QueryParam("idAgent") Long idAgent) {
		NodeVO vo = null;
		try {
			vo = service.getModuleFilterAgent(id,idAgent);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		List<NodeVO> list = new ArrayList<>();
		list.add(vo);
		return Response.ok(list).build();
	}

	@GET
	@Path(value = "/getinterviewmodule")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getInterviewModule(@QueryParam("id") Long id) {
		List<ModuleVO> list = new ArrayList<ModuleVO>();
		boolean isIntroModule = false;
		try {
			SystemPropertyVO vo = null;
			if (id == -1) {
				vo = sysPropService.getByName(Constant.STUDY_INTRO);
				if(vo !=null){
				   if(NumberUtils.isNumber(vo.getValue())){
					   id = Long.valueOf(vo.getValue());
				   }else{
					   return Response.status(Status.EXPECTATION_FAILED).type("text/plain").
								entity("Verify that "+Constant.STUDY_INTRO +" in System Config is a number.").build();
				   }
				}
			}
			if(id == -1 && vo == null){
				return Response.status(Status.EXPECTATION_FAILED).type("text/plain").
						entity("Unable to find "+Constant.STUDY_INTRO +" in System Config.").build();
			}
			list = service.findByIdForInterview(id);
			if (isIntroModule && list.get(0) == null) {
				return Response.status(Status.BAD_REQUEST).type("text/plain")
						.entity("Study intro module does not exist:" + id).build();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(list).build();
	}
	
	@Path(value = "/create")
	@POST
	@Consumes(value = MediaType.APPLICATION_JSON_VALUE)
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response create(ModuleVO json) {
		try {
			service.create(json);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}
	
	@Path(value="/setActiveIntroModule")
	@POST
    @Consumes(value=MediaType.APPLICATION_JSON_VALUE)
    @Produces(value=MediaType.APPLICATION_JSON_VALUE)
	public Response setActiveIntroModule(ModuleVO vo) {
		try{
			service.setActiveIntroModule(vo);
		}catch(Throwable e){
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@Path(value = "/update")
	@POST
	@Consumes(value = MediaType.APPLICATION_JSON_VALUE)
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response update(ModuleVO json) {
		try {
			service.update(json);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@Path(value = "/delete")
	@POST
	public Response delete(ModuleVO json) {
		try {
			service.merge(json);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@Path(value = "/saveAs")
	@POST
	public Response saveCopy(ModuleCopyVO json) {
		NodeRuleHolder idNodeHolder = null;
		try {
			idNodeHolder = service.copyModule(json);
			if (json.isIncludeRules()) {
				service.copyRules(idNodeHolder);
				service.addNodeRules(idNodeHolder);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(idNodeHolder.getIdNode()).build();
	}

	@Path(value = "/importJson")
	@POST
	public Response importJson(FormDataMultiPart multiPart) {
		ModuleReportVO report = new ModuleReportVO();
		try {
			Map<String, List<FormDataBodyPart>> fieldsByName = multiPart.getFields();

			for (List<FormDataBodyPart> fields : fieldsByName.values()) {
				for (FormDataBodyPart field : fields) {
					InputStream in = field.getEntityAs(InputStream.class);
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
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
					if("M_IntroModule".equals(vo.getType())){
						idNodeHolder = service.copyModuleAutoGenerateModule(copyVo,report);
						service.updateMissingLinks(copyVo.getVo());
						for(ModuleVO moduleVO:copyVo.getModules()){
							service.updateMissingLinks(moduleVO);
						}
					}else{
						// this is for fragment
						idNodeHolder = service.copyModuleAutoGenerateFragments(copyVo,report);
						service.updateMissingLinks(copyVo.getVo());
						for(FragmentVO fragmentVO:copyVo.getFragments()){
							service.updateMissingLinks(fragmentVO);
						}
					}
					//missing rules report
					service.copyRulesValidateAgent(idNodeHolder,report);
					service.addNodeRulesValidateAgent(idNodeHolder,report);
					// adding module vo to the report
					report.setVo(vo);
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}

		return Response.ok(report).build();
	}
	
	@GET
	@Path(value = "/getAllModulesReport")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllModulesReport(){
		List<ModuleReportVO> report = new ArrayList<>();
		
		List<Module> modules;
		try {
			 modules = service.getAllModules();
			 report = generateReport(modules);
			
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(report).build();
	}
	
	@GET
	@Path(value = "/getNodeNameById")
	@Produces(value = MediaType.APPLICATION_JSON_VALUE)
	public Response getNodeNameById(@QueryParam("id") Long idNode){
		NodeVO nodeVO = null;
		try {
			nodeVO = service.getNodeNameById(idNode);
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		}
		return Response.ok(nodeVO).build();
	}

	private List<ModuleReportVO> generateReport(List<Module> modules) {
		List<ModuleReportVO> reports = new ArrayList();
		
		for(Module module : modules){
			
			ModuleReportVO report = new ModuleReportVO();
			ModuleVO vo = new ModuleVO();
			vo.setName(module.getName());
			vo.setIdNode(module.getIdNode());
			report.setVo(vo);;
			populateQuestions(module.getChildNodes(), report);
			reports.add(report);			
		}		
		
		return reports;
	}

	private void populateQuestions(List<Question> childNodes,
			ModuleReportVO report) {
		
		for (Question vo : childNodes) {
			report.setTotalQuestions(report.getTotalQuestions() + 1);
			if (!vo.getChildNodes().isEmpty()) {
				populateAnswers(vo.getChildNodes(), report);
			}
		}
	}
	
	private void populateAnswers(List<PossibleAnswer> childNodes,
			ModuleReportVO report) {
	
		for (PossibleAnswer vo : childNodes) {
			report.setTotalAnswers(report.getTotalAnswers()+1);
			
			if(vo.getType().equals("P_freetext") && !pattern.matcher(vo.getName()).find()){
				report.addIssue(vo.getIdNode()+" "+vo.getNumber() +" "+vo.getName());				
			}
			
			if (!vo.getModuleRule().isEmpty()) {
				populateRules(vo.getModuleRule(), report);
			}
			if (!vo.getChildNodes().isEmpty()) {
				populateQuestions(vo.getChildNodes(), report);
			}
		}
	}

	private void populateRules(List<ModuleRule> moduleRule, ModuleReportVO report) {
		report.setTotalRules(moduleRule.size() + report.getTotalRules());
	}
}
