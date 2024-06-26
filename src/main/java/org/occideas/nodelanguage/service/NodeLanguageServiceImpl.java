package org.occideas.nodelanguage.service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.*;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.NodeLanguageMapper;
import org.occideas.nodelanguage.dao.INodeLanguageDao;
import org.occideas.utilities.NodeLanguageUtil;
import org.occideas.vo.LanguageVO;
import org.occideas.vo.NodeLanguageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NodeLanguageServiceImpl implements NodeLanguageService {

  private Logger log = LogManager.getLogger(this.getClass());

  @Autowired
  private INodeLanguageDao dao;
  @Autowired
  private NodeLanguageMapper mapper;
  @Autowired
  private ModuleMapper modMapper;
  @Autowired
  private NodeLanguageUtil nodeLanguageUtil;


  @Override
  public void save(NodeLanguageVO nodeLanguageVO) {
    NodeLanguage nodeLanguage = dao.getNodeLanguageByWordAndLanguage(nodeLanguageVO.getWord(),
      nodeLanguageVO.getLanguageId());
    if (nodeLanguage != null) {
      nodeLanguageVO.setId(nodeLanguage.getId());
    }
    if (nodeLanguage != null && nodeLanguage.getTranslation().equals(nodeLanguageVO.getTranslation())) {
      return;
    }
    dao.save(mapper.convertToNodeLanguage(nodeLanguageVO));
  }


  @Override
  public void batchSave(List<NodeLanguageVO> list) {
    dao.batchSave(mapper.convertToNodeLanguageList(list));
  }


  @Override
  public List<NodeLanguageVO> getNodesByLanguage(String language) {
    return mapper.convertToNodeLanguageVOList(dao.getNodesByLanguage(language));
  }


  @Override
  public void addLanguage(LanguageVO vo) {
    dao.addLanguage(mapper.convertToLanguage(vo));
  }


  @Override
  public List<LanguageVO> getAllLanguage() {
    return mapper.convertToListLanguageVO(dao.getAllLanguage());
  }


  @Override
  public List<NodeLanguageVO> getNodeLanguageById(String id) {
    Instant start = Instant.now();
    List<NodeLanguage> nodeLanguageByIdList = dao.getNodeLanguageById(Long.valueOf(id));
    Instant end = Instant.now();
    log.info("getNodeLanguageById -" + Duration.between(start, end));
    Instant start2 = Instant.now();
    List<NodeLanguageVO> convertToNodeLanguageVOListOnly = mapper
      .convertToNodeLanguageVOListOnly(nodeLanguageByIdList);
    Instant end2 = Instant.now();
    log.info("convertToNodeLanguageVOListOnly -" + Duration.between(start2, end2));
    return convertToNodeLanguageVOListOnly;
  }

  @Override
  public List<NodeLanguageVO> getNodeLanguageByIdandLanguage(Long id, Long languageid) {
    Instant start = Instant.now();
    Instant end = Instant.now();
    log.info("getNodeLanguageById -" + Duration.between(start, end));
    Instant start2 = Instant.now();
    List<NodeLanguageVO> convertToNodeLanguageVOListOnly = null;
    try {
      convertToNodeLanguageVOListOnly = nodeLanguageUtil.getLanguageJson("SA", String.valueOf(id));
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
    Instant end2 = Instant.now();
    log.info("convertToNodeLanguageVOListOnly -" + Duration.between(start2, end2));
    return convertToNodeLanguageVOListOnly;
  }


  @Override
  public void delete(NodeLanguageVO vo) {
    dao.delete(mapper.convertToNodeLanguage(vo));
  }


  @Override
  public NodeLanguageVO getNodesByLanguageAndWord(long getLanguageId, String word) {
    return mapper.convertToNodeLanguageVO(dao.getNodesByLanguageAndWord(getLanguageId, word));
  }


  @Override
  public List<LanguageVO> getDistinctLanguage() {
    List<Long> distinctNodeLanguageId = dao.getDistinctNodeLanguageId();
    if (distinctNodeLanguageId.isEmpty()) {
      return new ArrayList<>();
    }
    List<Language> distinctLanguage = dao.getDistinctLanguage(distinctNodeLanguageId);
    return mapper.convertToListLanguageVO(distinctLanguage);
  }


  @Override
  public LanguageVO getLanguageById(Long id) {
    return mapper.convertToLanguageVO(dao.getLanguageById(id));
  }


  @Override
  public List<NodeNodeLanguageMod> getNodeNodeLanguageListMod() {
    return dao.getNodeNodeLanguageListMod();
  }


  @Override
  public List<NodeNodeLanguageFrag> getNodeNodeLanguageFragList() {
    return dao.getNodeNodeLanguageListFrag();
  }


  @Override
  public Integer getUntranslatedModules(String flag) {
    List<JobModule> untranslatedModules = dao.getUntranslatedModules(flag);
    if (untranslatedModules == null) {
      return 0;
    }
    return untranslatedModules.size();
  }


  @Override
  public Integer getTotalUntranslatedModule() {
    return dao.getTotalUntranslatedModule();
  }


  @Override
  public Integer getTotalModuleCount() {
    return dao.getTotalModuleCount();
  }


  @Override
  public Integer getTotalFragmentCount() {
    return dao.getTotalFragmentCount();
  }


  @Override
  public Integer getUntranslatedFragments(String flag) {
    List<Fragment> untranslatedFragments = dao.getUntranslatedFragments(flag);
    if (untranslatedFragments == null) {
      return 0;
    }
    return untranslatedFragments.size();
  }


  @Override
  public Integer getTotalUntranslatedFragment() {
    return dao.getTotalUntranslatedFragment();
  }


  @Override
  public List<LanguageModBreakdown> getLanguageModBreakdown(String flag) {
    return dao.getLanguageModBreakdown(flag);
  }


  @Override
  public List<LanguageFragBreakdown> getLanguageFragBreakdown(String flag) {
    return dao.getLanguageFragBreakdown(flag);
  }


  @Override
  public void batchSaveJson(String idNode, String language, List<NodeLanguageVO> vo) {
    try {
      nodeLanguageUtil.createNodeLanguageJson(idNode, language, vo, true);
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
