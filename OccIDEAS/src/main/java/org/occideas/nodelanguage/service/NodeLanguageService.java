package org.occideas.nodelanguage.service;

import java.util.List;

import org.occideas.entity.LanguageFragBreakdown;
import org.occideas.entity.LanguageModBreakdown;
import org.occideas.entity.NodeNodeLanguageFrag;
import org.occideas.entity.NodeNodeLanguageMod;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.LanguageVO;
import org.occideas.vo.NodeLanguageVO;

public interface NodeLanguageService {

	void save(NodeLanguageVO nodeLanguageVO);
	
	List<NodeLanguageVO> getNodesByLanguage(String language);
	
	public void addLanguage(LanguageVO vo);
	
	public List<LanguageVO> getAllLanguage();

	List<NodeLanguageVO> getNodeLanguageById(String id);
	
	public NodeLanguageVO getNodesByLanguageAndWord(long getLanguageId, String word);
	
	public void delete(NodeLanguageVO vo);
	
	public List<LanguageVO> getDistinctLanguage();

	public LanguageVO getLanguageById(Long id);
	
	public List<NodeNodeLanguageMod> getNodeNodeLanguageListMod();

	Integer getUntranslatedModules(String flag);

	Integer getTotalUntranslatedModule();
	
	Integer getTotalModuleCount();

	List<NodeNodeLanguageFrag> getNodeNodeLanguageFragList();
	
	Integer getTotalFragmentCount();

	Integer getUntranslatedFragments(String flag);

	Integer getTotalUntranslatedFragment();
	
	List<LanguageModBreakdown> getLanguageModBreakdown(String flag);

	List<LanguageFragBreakdown> getLanguageFragBreakdown(String flag);
}
