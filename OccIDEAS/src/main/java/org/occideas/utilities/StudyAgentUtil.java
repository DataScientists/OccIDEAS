package org.occideas.utilities;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.Constant;
import org.occideas.module.service.ModuleService;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class StudyAgentUtil {

	private Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	ServletContext context;
	
	@Autowired
	private SystemPropertyService systemPropertyService;
	@Autowired
	private ModuleService moduleService;

	public ModuleVO getStudyAgentJson(String idNode) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String path = System.getProperty("user.home");
		File file = new File(path+"/modules/"+idNode + ".json");
		ModuleVO modVO = mapper.readValue(file, ModuleVO.class);
		return modVO;
	}
	public FragmentVO getStudyAgentFragmentJson(String idNode) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String path = System.getProperty("user.home");
		FragmentVO modVO = mapper.readValue(new File(path+"/modules/"+idNode + ".json"), FragmentVO.class);
		return modVO;
	}

	public boolean isStudyAgentJsonExist(Long idNode)
			throws JsonGenerationException, JsonMappingException, IOException {
		String path = System.getProperty("user.home");
		File file = new File(path+"/modules/"+idNode + ".json");
		return file.exists();
	}

	public String convertModuleStudyAgentToJsonString(ModuleVO studyAgent) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		// Object to JSON in String
		String jsonInString = mapper.writeValueAsString(studyAgent);
		return jsonInString;
	}

	public void createStudyAgentJson(String idNode, NodeVO vo,boolean override)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String path = System.getProperty("user.home");
		String filePath = path+"/modules/"+idNode + ".json";
		new File(path+"/modules/").mkdir();
		File expectedFile = new File(filePath);
		if(expectedFile.exists() && !override){
			log.info("expected file - "+filePath + " already exist.");
			return;
		}else{
		log.info("[Start] creating file - "+filePath);
		mapper.writeValue(expectedFile, vo);
		log.info("[End] creating file - "+filePath);
		}
	}
	
	public void deleteStudyAgentJson(String idNode){
		try{
		SystemPropertyVO autoCreateJson = systemPropertyService.getByName(Constant.AUTO_CREATE_STUDY_AGENT_JSON);
		if (autoCreateJson != null && "true".equals(autoCreateJson.getValue().toLowerCase().trim())) {
		if(doesStudyAgentJsonExist(idNode)){
			String path = System.getProperty("user.home");
			String filePath = path+"/modules/"+idNode + ".json";
			File expectedFile = new File(filePath);
			if(expectedFile.exists()){
				boolean deleted = expectedFile.delete();
				if(deleted){
					log.info(expectedFile+" has beed deleted.");
				}else{
					log.error("Unable to delete "+expectedFile);
				}
			}
		}
		}
		}catch(Throwable ex){
			log.error("Error on delete study agent json "+idNode,ex);
		}
	}
	
	
	public boolean doesStudyAgentJsonExist(String idNode)
			throws JsonGenerationException, JsonMappingException, IOException {
		String path = System.getProperty("user.home");
		String filePath = path+"/modules/"+idNode + ".json";
		new File(path+"/modules/").mkdir();
		File expectedFile = new File(filePath);
		if(expectedFile.exists()){
			log.info("expected file - "+filePath + " already exist.");
			return true;
		}else{
			return false;
		}
	}
	
	public NodeVO searchNode(NodeVO nodeVO, Long idNode)
			throws JsonGenerationException, JsonMappingException, IOException {
		NodeVO result = null;
		if (nodeVO instanceof ModuleVO) {
			ModuleVO mod = (ModuleVO) nodeVO;
			if (nodeVO.getIdNode() == idNode) {
				return nodeVO;
			} else {
				List<QuestionVO> childNodes = mod.getChildNodes();
				for (QuestionVO qVO : childNodes) {
					result = searchNode(qVO, idNode);
					if (result != null) {
						break;
					}
				}
			}
		}
		if (nodeVO instanceof FragmentVO) {
			FragmentVO frag = (FragmentVO) nodeVO;
			if (nodeVO.getIdNode() == idNode) {
				return nodeVO;
			} else {
				List<QuestionVO> childNodes = frag.getChildNodes();
				for (QuestionVO qVO : childNodes) {
					result = searchNode(qVO, idNode);
					if (result != null) {
						break;
					}
				}
			}
		}
		if (nodeVO instanceof QuestionVO) {
			QuestionVO qVO = (QuestionVO) nodeVO;
			if (nodeVO.getIdNode() == idNode) {
				return nodeVO;
			} else {
				List<PossibleAnswerVO> childNodes = qVO.getChildNodes();
				for (PossibleAnswerVO aVO : childNodes) {
					result = searchNode(aVO, idNode);
					if (result != null) {
						break;
					}
				}
			}
		}
		if (nodeVO instanceof PossibleAnswerVO) {
			PossibleAnswerVO aVO = (PossibleAnswerVO) nodeVO;
			if (nodeVO.getIdNode() == idNode) {
				return nodeVO;
			} else {
				List<QuestionVO> childNodes = aVO.getChildNodes();
				for (QuestionVO qVO : childNodes) {
					result = searchNode(qVO, idNode);
					if (result != null) {
						break;
					}
				}
			}
		}
		return result;
	}

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		StudyAgentUtil jsonUtil = new StudyAgentUtil();
		// System.out.println(jsonUtil.isStudyAgentJsonExist(123123L));
		// ModuleVO modVO = new ModuleVO();
		// modVO.setIdNode(111111L);
		// List<QuestionVO> list = new ArrayList<>();
		// QuestionVO qVO1 = new QuestionVO();
		// qVO1.setIdNode(123123L);
		// qVO1.setName("test");
		// QuestionVO qVO2 = new QuestionVO();
		// qVO2.setIdNode(666666L);
		// list.add(qVO1);
		// list.add(qVO2);
		// modVO.setChildNodes(list);
		try {
			// NodeVO node = jsonUtil.searchNode(modVO, 123123L);
			// System.out.println(node.getName());
			jsonUtil.createStudyAgentJson("44161", new ModuleVO(),true);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createStudyAgentForUpdatedNode(long idNode, String name) {
			SystemPropertyVO autoCreateJson = systemPropertyService.getByName(Constant.AUTO_CREATE_STUDY_AGENT_JSON);
			if (autoCreateJson != null && "true".equals(autoCreateJson.getValue().toLowerCase().trim())) {
				 NodeVO nodeVO = moduleService.getModuleFilterStudyAgent(idNode);
				 if(nodeVO instanceof ModuleVO){
					 ModuleVO moduleFilterStudyAgent = (ModuleVO) nodeVO;
					 try {
						 createStudyAgentJson(String.valueOf(idNode), moduleFilterStudyAgent,true);
					 } catch (Exception e) {
						 log.error("Error creating study agent module json for "
								 +name+"-"+idNode,e);
					 }
				 }else{
					 return;
				 }
			}
	}

}
