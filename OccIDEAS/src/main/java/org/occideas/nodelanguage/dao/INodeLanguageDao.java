package org.occideas.nodelanguage.dao;

import java.util.List;

import org.occideas.entity.Language;
import org.occideas.entity.Module;
import org.occideas.entity.NodeLanguage;
import org.occideas.entity.NodeNodeLanguageMod;

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

}
