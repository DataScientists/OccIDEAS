package org.occideas.utilities;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;

import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class StudyAgentUtil {

	@Autowired
	ServletContext context;

	public ModuleVO getStudyAgentJson(String idNode) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String path = context.getRealPath("");
		ModuleVO modVO = mapper.readValue(new File(path+"\\"+idNode + ".json"), ModuleVO.class);
		return modVO;
	}

	public boolean isStudyAgentJsonExist(Long idNode)
			throws JsonGenerationException, JsonMappingException, IOException {
		String path = context.getRealPath("");
		File file = new File(path+"\\"+idNode + ".json");
		return file.exists();
	}

	public String convertModuleStudyAgentToJsonString(ModuleVO studyAgent) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		// Object to JSON in String
		String jsonInString = mapper.writeValueAsString(studyAgent);
		return jsonInString;
	}

	public void createStudyAgentJson(String idNode, NodeVO vo)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String path = context.getRealPath("");
		mapper.writeValue(new File(path+"\\"+idNode + ".json"), vo);
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
			jsonUtil.createStudyAgentJson("44161", new ModuleVO());
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

}