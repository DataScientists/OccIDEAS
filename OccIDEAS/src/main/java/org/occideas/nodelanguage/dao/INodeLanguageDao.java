package org.occideas.nodelanguage.dao;

import java.util.List;

import org.occideas.entity.Fragment;
import org.occideas.entity.Language;
import org.occideas.entity.LanguageFragBreakdown;
import org.occideas.entity.LanguageModBreakdown;
import org.occideas.entity.Module;
import org.occideas.entity.NodeLanguage;
import org.occideas.entity.NodeNodeLanguageFrag;
import org.occideas.entity.NodeNodeLanguageMod;
import org.occideas.entity.Translate;

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

	List<Module> getUntranslatedModules(String flag);

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

}
