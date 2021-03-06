package org.occideas.nodelanguage.service;

import org.occideas.entity.LanguageFragBreakdown;
import org.occideas.entity.LanguageModBreakdown;
import org.occideas.entity.NodeNodeLanguageFrag;
import org.occideas.entity.NodeNodeLanguageMod;
import org.occideas.vo.LanguageVO;
import org.occideas.vo.NodeLanguageVO;

import java.util.List;

public interface NodeLanguageService {

  void save(NodeLanguageVO nodeLanguageVO);

  List<NodeLanguageVO> getNodesByLanguage(String language);

  void addLanguage(LanguageVO vo);

  List<LanguageVO> getAllLanguage();

  List<NodeLanguageVO> getNodeLanguageById(String id);

  NodeLanguageVO getNodesByLanguageAndWord(long getLanguageId, String word);

  void delete(NodeLanguageVO vo);

  List<LanguageVO> getDistinctLanguage();

  LanguageVO getLanguageById(Long id);

  List<NodeNodeLanguageMod> getNodeNodeLanguageListMod();

  Integer getUntranslatedModules(String flag);

  Integer getTotalUntranslatedModule();

  Integer getTotalModuleCount();

  List<NodeNodeLanguageFrag> getNodeNodeLanguageFragList();

  Integer getTotalFragmentCount();

  Integer getUntranslatedFragments(String flag);

  Integer getTotalUntranslatedFragment();

  List<LanguageModBreakdown> getLanguageModBreakdown(String flag);

  List<LanguageFragBreakdown> getLanguageFragBreakdown(String flag);

  void batchSave(List<NodeLanguageVO> nodeLanguageVO);

  void batchSaveJson(String idNode, String language, List<NodeLanguageVO> vo);

  List<NodeLanguageVO> getNodeLanguageByIdandLanguage(Long id, Long languageid);
}
