package org.occideas.nodelanguage.dao;

import org.occideas.entity.*;

import java.util.List;

public interface INodeLanguageDao {

  NodeLanguage getNodeLanguageByWordAndLanguage(String word, long languageId);

  NodeNodeLanguageMod getNodeNodeLanguageMod(long idNode, String flag);

  List<NodeNodeLanguageMod> getNodeNodeLanguageListMod();

  Language getLanguageById(Long id);

  List<Language> getDistinctLanguage(List<Long> ids);

  List<Long> getDistinctNodeLanguageId();

  NodeLanguage getNodesByLanguageAndWord(Long language, String word);

  List<NodeLanguage> getNodesByLanguage(String language);

  List<NodeLanguage> getNodeLanguageById(Long id);

  NodeLanguage get(Long id);

  void delete(NodeLanguage entity);

  void save(NodeLanguage entity);

  List<Language> getAllLanguage();

  void addLanguage(Language entity);

  List<JobModule> getUntranslatedModules(String flag);

  Integer getTotalUntranslatedModule();

  Integer getTotalModuleCount();

  List<NodeNodeLanguageFrag> getNodeNodeLanguageListFrag();

  Integer getTotalFragmentCount();

  List<Fragment> getUntranslatedFragments(String flag);

  Integer getTotalUntranslatedFragment();

  List<LanguageModBreakdown> getLanguageModBreakdown(String flag);

  List<LanguageFragBreakdown> getLanguageFragBreakdown(String flag);

  void batchSave(List<NodeLanguage> entityList);

  List<NodeLanguage> getNodeLanguageByIdandLanguage(Long id, Long languageid);

  void saveTranslate(Translate entity);

  List<String> getNodeLanguageWordsByIdOrderByWord(Long id);

  List<JobModule> getModulesIdNodeSQL();

  List<Fragment> getFragmentIdNodeSQL();

  void deleteAll();

}
