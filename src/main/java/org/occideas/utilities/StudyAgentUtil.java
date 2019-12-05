package org.occideas.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.Constant;
import org.occideas.module.service.ModuleService;
import org.occideas.node.service.INodeService;
import org.occideas.qsf.ApplicationQSF;
import org.occideas.qsf.BlockElement;
import org.occideas.qsf.QSFElementTypes;
import org.occideas.qsf.SurveyElement;
import org.occideas.qsf.SurveyEntry;
import org.occideas.qsf.payload.Choice;
import org.occideas.qsf.payload.Condition;
import org.occideas.qsf.payload.Configuration;
import org.occideas.qsf.payload.Default;
import org.occideas.qsf.payload.DisplayLogic;
import org.occideas.qsf.payload.Flow;
import org.occideas.qsf.payload.Logic;
import org.occideas.qsf.payload.Payload;
import org.occideas.qsf.payload.ProjectPayload;
import org.occideas.qsf.payload.Properties;
import org.occideas.qsf.payload.QuestionPayload;
import org.occideas.qsf.payload.ScoringPayload;
import org.occideas.qsf.payload.Setting;
import org.occideas.qsf.payload.StatisticsPayload;
import org.occideas.qsf.payload.SubPayload;
import org.occideas.qsf.payload.SurveyOptionPayload;
import org.occideas.qsf.payload.Trash;
import org.occideas.qsf.payload.Validation;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;

@Component
public class StudyAgentUtil {

	@Autowired
	ServletContext context;
	private Logger log = LogManager.getLogger(this.getClass());
	@Autowired
	private SystemPropertyService systemPropertyService;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private INodeService nodeService;

	private AtomicInteger qidCount;

	public ModuleVO getStudyAgentJson(String idNode) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File file = getJsonFile(idNode);
		ModuleVO modVO = mapper.readValue(file, ModuleVO.class);
		return modVO;
	}

	public File convertToApplicationQSF(String idNode) throws IOException {
		List<ModuleVO> moduleList = moduleService.findById(Long.valueOf(idNode));
		if (!moduleList.isEmpty()) {
			return moduleToApplicationQSF(moduleList.get(0));
		} else {
			return null;
		}
	}

	public FragmentVO getStudyAgentFragmentJson(String idNode) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String path = "/opt/data";
		FragmentVO modVO = mapper.readValue(new File(path + "/modules/" + idNode + ".json"), FragmentVO.class);
		return modVO;
	}

	public File moduleToApplicationQSF(ModuleVO module) throws IOException {
		qidCount = new AtomicInteger(0);
		String path = "/opt/data";
		File directory = new File(path + "/qsf");
		if (!directory.exists()) {
			directory.mkdirs();
		}

		File file = new File(
				path + "/qsf/" + module.getName().substring(0, 4) + "_" + System.currentTimeMillis() + ".qsf");
		if (!file.exists()) {
			file.createNewFile();
		}

		ApplicationQSF applicationQSF = new ApplicationQSF();
		final String surveyId = "SV_3W2wSyxLDan2ZPD";
		applicationQSF.setSurveyEntry(new SurveyEntry(surveyId, "SimpleSurvey", null, "UR_es2Gulf6I7xN6p7", "curtin",
				null, "EN", "RS_cMiHcMFlPoWrvyl", "Inactive", "0000-00-00 00:00:00", "0000-00-00 00:00:00",
				"2019-11-22 02:22:39", "UR_es2Gulf6I7xN6p7", "2019-11-22 02:25:12", "0000-00-00 00:00:00",
				"0000-00-00 00:00:00", null));

		FileWriter fw = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(applicationQSF.toString().substring(0, applicationQSF.toString().length() - 1));
		bw.write(",");
		// List<SurveyElement> surveyElements = new ArrayList<>();
		List<Flow> flows = new ArrayList<>();
		flows.add(new Flow("BL_agDgwnIMsiB1KJL", "Block", "FL_2"));
		SurveyElement flowElement = new SurveyElement(surveyId, QSFElementTypes.FLOW.getAbbr(),
				QSFElementTypes.FLOW.getDesc(), null, null,
				buildPayload(new SubPayload(flows, new Properties(2), "FL_1", "Root")));

		SurveyElement surveyOptions = new SurveyElement(surveyId, QSFElementTypes.OPTIONS.getAbbr(),
				QSFElementTypes.OPTIONS.getDesc(), null, null,
				buildPayload(new SurveyOptionPayload("false", "true", "PublicSurvey", "false", "Yes", "true", "None",
						"DefaultMessage", "", "", "None", "+1 week", "", "\u2190", "\u2192", "curtin", "MQ", "curtin1",
						1)));

		SurveyElement scoringElement = new SurveyElement(surveyId, QSFElementTypes.SCORING.getAbbr(),
				QSFElementTypes.SCORING.getDesc(), null, null,
				buildPayload(new ScoringPayload(new ArrayList<>(), new ArrayList<>(), null, 0, 0, null, null)));

		SurveyElement projElement = new SurveyElement(surveyId, QSFElementTypes.PROJECT.getAbbr(),
				QSFElementTypes.PROJECT.getDesc(), null, "1.1.0", buildPayload(new ProjectPayload("CORE", "1.1.0")));

		SurveyElement statElement = new SurveyElement(surveyId, QSFElementTypes.STATISTICS.getAbbr(),
				QSFElementTypes.STATISTICS.getDesc(), null, null,
				buildPayload(new StatisticsPayload(true, "Survey Statistics")));

		SurveyElement defaultResponseElement = new SurveyElement(surveyId, QSFElementTypes.RESPONSESET.getAbbr(),
				"RS_cMiHcMFlPoWrvyl", QSFElementTypes.RESPONSESET.getDesc(), null, null);

		String questions = buildQuestions(module, surveyId);
		// new questions to be added to block elements - link modules
		SurveyElement blockElement = new SurveyElement(surveyId, QSFElementTypes.BLOCK.getAbbr(),
				QSFElementTypes.BLOCK.getDesc(), null, null,
				buildPayload(
						new Default("Default", "Default Question Block", "BL_agDgwnIMsiB1KJL",
								buildBlockElements(qidCount.intValue(), module.getName())),
						new Trash("Trash", "Trash \\/ Unused Questions", "BL_4I380AxUd3s3gt7")));
		bw.write("\"SurveyElements\":[");
		bw.write(blockElement.toString());
		bw.write(",");
		bw.write(flowElement.toString());
		bw.write(",");
		bw.write(surveyOptions.toString());
		bw.write(",");
		bw.write(scoringElement.toString());
		bw.write(",");
		bw.write(projElement.toString());
		bw.write(",");
		bw.write(statElement.toString());
		SurveyElement questionCountElement = new SurveyElement(surveyId, QSFElementTypes.QUESTIONCOUNT.getAbbr(),
				QSFElementTypes.QUESTIONCOUNT.getDesc(), String.valueOf(qidCount.intValue()), null, null);
		bw.write(",");
		bw.write(questionCountElement.toString());
		bw.write(",");
		bw.write(defaultResponseElement.toString());
		bw.write(",");
		bw.write(questions.substring(0, questions.length()-1));
		
		bw.write("]}");
		bw.close();

		return file;
	}

	private String buildQuestions(ModuleVO module, final String surveyId) {
		StringBuilder sb = new StringBuilder();
		for (QuestionVO qVO : module.getChildNodes()) {
			createQuestion(module, surveyId, qVO, sb, null);
		}
		return sb.toString().replace("-999999", String.valueOf(qidCount.intValue() + 1));
	}

	private void createQuestion(NodeVO node, final String surveyId, QuestionVO qVO, StringBuilder sb,
			DisplayLogic logic) {
		if (qVO.getLink() == 0L) {
			String questionTxt = node.getName().substring(0, 4) + "_" + qVO.getNumber() + " - " + qVO.getName();
			String qidStrCount = "QID" + qidCount.incrementAndGet();
			Object payload = null;
			if (logic == null) {
				payload = buildPayload(new QuestionPayload(questionTxt, qVO.getNumber(), "MC", "SAVR", "TX",
						new Configuration("UseText"), qVO.getName(), buildChoices(qVO, node.getName()),
						buildChoiceOrder(qVO), new Validation(new Setting("OFF", "ON", "None")), new ArrayList<>(), -999999,
						1, qidStrCount, logic));
			} else {
				payload = buildPayload(new QuestionPayload(questionTxt, qVO.getNumber(), "MC", "SAVR", "TX",
						new Configuration("UseText"), qVO.getName(), buildChoices(qVO, node.getName()),
						buildChoiceOrder(qVO), new Validation(new Setting("OFF", "ON", "None")), new ArrayList<>(), -999999,
						1, qidStrCount, logic));
			}
			SurveyElement question = new SurveyElement(surveyId, QSFElementTypes.QUESTION.getAbbr(), qidStrCount,
					questionTxt, null, payload);
			sb.append(question.toString());
			sb.append(",");
		}
		int ansCount = 1;
		for (PossibleAnswerVO answer : qVO.getChildNodes()) {
			if (!answer.getChildNodes().isEmpty()) {
				for (QuestionVO childQuestionVO : answer.getChildNodes()) {
					if (childQuestionVO.getLink() == 0L) {
						String childQidCount = "QID" + qidCount.intValue();
						String choiceLocator = "q://" + childQidCount + "/SelectableChoice/" + ansCount;
						createQuestion(node, surveyId, childQuestionVO, sb,
								new DisplayLogic("BooleanExpression", false, new Condition(
										new Logic("Question", childQidCount, "no", choiceLocator, "Selected",
												childQidCount, choiceLocator, "Expression", childQuestionVO.getName()),
										"If")));
					} else if (childQuestionVO.getLink() != 0L) {
						NodeVO linkModule = nodeService.getNode(childQuestionVO.getLink());
						String childQidCount = "QID" + qidCount.intValue();
						String choiceLocator = "q://" + childQidCount + "/SelectableChoice/" + ansCount;
						if ("F".equals(linkModule.getNodeclass())) {
							for (QuestionVO linkQuestion : ((FragmentVO) linkModule).getChildNodes()) {
								createQuestion(node, surveyId, linkQuestion, sb,
										new DisplayLogic("BooleanExpression", false,
												new Condition(new Logic("Question", childQidCount, "no", choiceLocator,
														"Selected", childQidCount, choiceLocator, "Expression",
														childQuestionVO.getName()), "If")));
							}
						} else {
							for (QuestionVO linkQuestion : ((ModuleVO) linkModule).getChildNodes()) {
								createQuestion(linkModule, surveyId, linkQuestion, sb,
										new DisplayLogic("BooleanExpression", false,
												new Condition(new Logic("Question", childQidCount, "no", choiceLocator,
														"Selected", childQidCount, choiceLocator, "Expression",
														childQuestionVO.getName()), "If")));
							}
						}

					}
				}
			}
			ansCount++;
		}
	}

	private String[] buildChoiceOrder(QuestionVO qVO) {
		String[] choiceOrder = new String[qVO.getChildNodes().size()];
		int count = 1;
		for (int i = 0; i < qVO.getChildNodes().size(); i++) {
			choiceOrder[i] = String.valueOf(count);
			count++;
		}
		return choiceOrder;
	}

	// choices should be object s of object and not array
	private List<Choice> buildChoices(QuestionVO qVO, String name) {
		List<Choice> choiceList = new ArrayList<>();
		for (PossibleAnswerVO answerVO : qVO.getChildNodes()) {
			Choice choice = new Choice(name.substring(0, 4) + "_" + answerVO.getNumber() + "_" + answerVO.getName());
			choiceList.add(choice);
		}
		return choiceList;
	}

	private List<BlockElement> buildBlockElements(int qidCount, String name) {
		List<BlockElement> blockElements = new ArrayList<>();
		int count = 0;
		while (count < qidCount) {
			blockElements.add(new BlockElement("Question", "QID" + (++count)));
			if (count != qidCount) {
				blockElements.add(new BlockElement("Page Break", null));
			}
		}
		return blockElements;
	}

	private Object buildPayload(Payload... payloads) {
		if (payloads.length <= 1) {
			return payloads[0];
		}
		List<Payload> payloadList = new ArrayList<>();
		for (Payload payload : payloads) {
			payloadList.add(payload);
		}
		return payloadList;
	}

	public boolean isStudyAgentJsonExist(Long idNode) throws IOException {
		String path = "/opt/data";
		File file = new File(path + "/modules/" + idNode + ".json");
		return file.exists();
	}

	public void createStudyAgentJson(String idNode, NodeVO vo, boolean override) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String filePath = createJsonFilePath(idNode);
		File expectedFile = new File(filePath);
		if (expectedFile.exists() && !override) {
			log.info("expected file - " + filePath + " already exist.");
			return;
		} else {
			log.info("[Start] creating file - " + filePath);
			mapper.writeValue(expectedFile, vo);
			log.info("[End] creating file - " + filePath);
		}
	}

	public String[] getStudyAgentCSV(String idNode) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(getCSVFile(idNode)));
		List<String[]> list = reader.readAll();
		reader.close();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	private File getCSVFile(String idNode) {
		String path = "/opt/data";
		File file = new File(path + "/modules/" + idNode + ".csv");
		return file;
	}

	private File getJsonFile(String idNode) {
		String path = "/opt/data";
		File file = new File(path + "/modules/" + idNode + ".json");
		return file;
	}

	public void createStudyAgentCSV(String idNode, List<String> list, boolean override) throws IOException {
		String filePath = createCSVFilePath(idNode);
		File expectedFile = new File(filePath);
		if (expectedFile.exists() && !override) {
			log.info("expected file - " + filePath + " already exist.");
		} else {
			if (list != null && !list.isEmpty()) {
				list = list.stream().sorted().collect(Collectors.toList());
				writeNewCSVFile(list, filePath, expectedFile);
			}
		}
	}

	private void writeNewCSVFile(List<String> list, String filePath, File expectedFile) throws IOException {
		FileWriter writer = new FileWriter(expectedFile);
		log.info("[Start] creating file - " + filePath);
		String commaDelimitedString = String.join(",", list);
		writer.write(commaDelimitedString);
		writer.close();
		log.info("[End] creating file - " + filePath);
	}

	public boolean doesIdNodeExistInArray(String[] arrayToSearch, String idNode) {
		return binarySearch(arrayToSearch, idNode) != -1;
	}

	private int binarySearch(String[] arrayToSearch, String value) {
		int low = 0;
		int high = arrayToSearch.length - 1;
		int mid;

		while (low <= high) {
			mid = (low + high) / 2;

			if (arrayToSearch[mid].compareTo(value) < 0) {
				low = mid + 1;
			} else if (arrayToSearch[mid].compareTo(value) > 0) {
				high = mid - 1;
			} else {
				return mid;
			}
		}

		return -1;
	}

	private String createCSVFilePath(String idNode) {
		String path = "/opt/data";
		String filePath = path + "/modules/" + idNode + ".csv";
		new File(path + "/modules/").mkdir();
		return filePath;
	}

	private String createJsonFilePath(String idNode) {
		String path = "/opt/data";
		String filePath = path + "/modules/" + idNode + ".json";
		new File(path + "/modules/").mkdir();
		return filePath;
	}

	public void purgeStudyAgentFiles() {
		String path = "/opt/data";
		String filePath = path + "/modules/";
		File dir = new File(filePath);
		File[] files = dir.listFiles();
		if (files == null)
			return;
		for (File file : files) {
			if ("csv".equals(FilenameUtils.getExtension(file.getName()))) {
				file.delete();
				log.info(file.getAbsolutePath() + " has beed deleted.");
			}
		}
	}

	public void deleteStudyAgentJson(String idNode) {
		try {
			SystemPropertyVO autoCreateJson = systemPropertyService.getByName(Constant.AUTO_CREATE_STUDY_AGENT_JSON);
			if (autoCreateJson != null && "true".equals(autoCreateJson.getValue().toLowerCase().trim())) {
				if (doesStudyAgentJsonExist(idNode)) {
					String path = "/opt/data";
					String filePath = path + "/modules/" + idNode + ".json";
					File expectedFile = new File(filePath);
					if (expectedFile.exists()) {
						boolean deleted = expectedFile.delete();
						if (deleted) {
							log.info(expectedFile + " has beed deleted.");
						} else {
							log.error("Unable to delete " + expectedFile);
						}
					}
				}
			}
		} catch (Throwable ex) {
			log.error("Error on delete study agent json " + idNode, ex);
		}
	}

	public boolean doesStudyAgentJsonExist(String idNode) throws IOException {
		String filePath = createJsonFilePath(idNode);
		File expectedFile = new File(filePath);
		if (expectedFile.exists()) {
			log.info("expected file - " + filePath + " already exist.");
			return true;
		} else {
			return false;
		}
	}

	public boolean doesStudyAgentCSVExist(String idNode) throws IOException {
		String filePath = createCSVFilePath(idNode);
		File expectedFile = new File(filePath);
		if (expectedFile.exists()) {
			log.info("expected file - " + filePath + " already exist.");
			return true;
		} else {
			return false;
		}
	}

	public NodeVO searchNode(NodeVO nodeVO, Long idNode) throws IOException {
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

	public void createStudyAgentForUpdatedNode(long idNode, String name) {
		SystemPropertyVO autoCreateJson = systemPropertyService.getByName(Constant.AUTO_CREATE_STUDY_AGENT_JSON);
		if (autoCreateJson != null && "true".equals(autoCreateJson.getValue().toLowerCase().trim())) {
			NodeVO nodeVO = moduleService.getModuleFilterStudyAgent(idNode);
			if (nodeVO instanceof ModuleVO) {
				ModuleVO moduleFilterStudyAgent = (ModuleVO) nodeVO;
				List<String> listOfIdNodes = new ArrayList<>();
				systemPropertyService.listAllQId(listOfIdNodes, moduleFilterStudyAgent);
				try {
					createStudyAgentJson(String.valueOf(idNode), moduleFilterStudyAgent, true);
					createStudyAgentCSV(String.valueOf(idNode), listOfIdNodes, true);
				} catch (Exception e) {
					log.error("Error creating study agent module json for " + name + "-" + idNode, e);
				}
			} else {
				return;
			}
		}
	}

	// public static void main(String[] args) {
	// StudyAgentUtil studyAgentUtil = new StudyAgentUtil();
	// studyAgentUtil.purgeStudyAgentFiles();
	// }

}
