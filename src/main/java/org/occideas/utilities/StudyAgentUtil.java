package org.occideas.utilities;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.Constant;
import org.occideas.module.service.ModuleService;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudyAgentUtil {

  @Autowired
  ServletContext context;
  private Logger log = LogManager.getLogger(this.getClass());
  @Autowired
  private SystemPropertyService systemPropertyService;
  @Autowired
  private ModuleService moduleService;


  public ModuleVO getStudyAgentJson(String idNode) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    File file = getJsonFile(idNode);
    ModuleVO modVO = mapper.readValue(file, ModuleVO.class);
    return modVO;
  }


  public FragmentVO getStudyAgentFragmentJson(String idNode) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String path = "/opt/data";
    FragmentVO modVO = mapper.readValue(new File(path + "/modules/" + idNode + ".json"), FragmentVO.class);
    return modVO;
  }


  public boolean isStudyAgentJsonExist(Long idNode)
    throws IOException {
    String path = "/opt/data";
    File file = new File(path + "/modules/" + idNode + ".json");
    return file.exists();
  }


  public String convertModuleStudyAgentToJsonString(ModuleVO studyAgent) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    // Object to JSON in String
    String jsonInString = mapper.writeValueAsString(studyAgent);
    return jsonInString;
  }


  public void createStudyAgentJson(String idNode, NodeVO vo, boolean override)
    throws IOException {
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


  public boolean doesStudyAgentJsonExist(String idNode)
    throws IOException {
    String filePath = createJsonFilePath(idNode);
    File expectedFile = new File(filePath);
    if (expectedFile.exists()) {
      log.info("expected file - " + filePath + " already exist.");
      return true;
    } else {
      return false;
    }
  }

  public boolean doesStudyAgentCSVExist(String idNode)
    throws IOException {
    String filePath = createCSVFilePath(idNode);
    File expectedFile = new File(filePath);
    if (expectedFile.exists()) {
      log.info("expected file - " + filePath + " already exist.");
      return true;
    } else {
      return false;
    }
  }


  public NodeVO searchNode(NodeVO nodeVO, Long idNode)
    throws IOException {
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
          log.error("Error creating study agent module json for "
            + name + "-" + idNode, e);
        }
      } else {
        return;
      }
    }
  }

//    public static void main(String[] args) {
//    	StudyAgentUtil studyAgentUtil = new StudyAgentUtil();
//    	studyAgentUtil.purgeStudyAgentFiles();
//    }

}
