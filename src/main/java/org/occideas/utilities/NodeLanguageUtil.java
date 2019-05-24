package org.occideas.utilities;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.vo.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class NodeLanguageUtil {

  private static final String LANGUAGE_FOLDER = "/language/";
  private static final String USER_HOME = "user.home";

  private Logger log = LogManager.getLogger(this.getClass());

  public List<NodeLanguageVO> getLanguageJson(String language, String idNode) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String path = System.getProperty(USER_HOME);
    StringBuilder stringBuilder = createLanguagePath(language, idNode, path);
    File file = new File(stringBuilder.toString());
    List<NodeLanguageVO> vo = mapper.readValue(file, new TypeReference<List<NodeLanguageVO>>() {
    });
    return vo;
  }


  private StringBuilder createLanguagePath(String language, String idNode, String path) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(path);
    stringBuilder.append(LANGUAGE_FOLDER);
    stringBuilder.append(language);
    stringBuilder.append("/");
    stringBuilder.append(idNode);
    stringBuilder.append(".json");
    return stringBuilder;
  }


  public void createNodeLanguageJson(String idNode, String language, List<NodeLanguageVO> list, boolean override)
    throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String path = System.getProperty(USER_HOME);
    File dir = new File(path + LANGUAGE_FOLDER + "/" + language);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    String filePath = createLanguagePath(language, idNode, path).toString();
    File expectedFile = new File(filePath);
    if (expectedFile.exists() && !override) {
      log.info("expected file - " + filePath + " already exist.");
      return;
    } else {
      log.info("[Start] creating file - " + filePath);
      mapper.writeValue(expectedFile, list);
      log.info("[End] creating file - " + filePath);
    }
  }

  public void deleteNodeLanguageJson(String idNode, String language) {
    try {
      if (doesNodeLanguageJsonExist(idNode, language)) {
        String path = System.getProperty("user.home");
        StringBuilder stringBuilder = createLanguagePath(language, idNode, path);
        File expectedFile = new File(stringBuilder.toString());
        if (expectedFile.exists()) {
          boolean deleted = expectedFile.delete();
          if (deleted) {
            log.info(expectedFile + " has beed deleted.");
          } else {
            log.error("Unable to delete " + expectedFile);
          }
        }
      }
    } catch (Throwable ex) {
      log.error("Error on delete study agent json " + idNode, ex);
    }
  }


  public boolean doesNodeLanguageJsonExist(String idNode, String language)
    throws IOException {
    String path = System.getProperty("user.home");
    StringBuilder stringBuilder = createLanguagePath(language, idNode, path);
    File file = new File(stringBuilder.toString());
    File dir = new File(path + LANGUAGE_FOLDER + "/" + language);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    if (file.exists()) {
      log.info("expected file - " + stringBuilder.toString() + " already exist.");
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


//    public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException{
//        NodeLanguageUtil languageUtil = new NodeLanguageUtil();
//        System.out.println(languageUtil.getLanguageJson("SA","44161"));
//    }

}
