package org.occideas.nodelanguage.service;

import java.util.List;

import org.occideas.vo.LanguageVO;
import org.occideas.vo.NodeLanguageVO;

public interface NodeLanguageService {

	void save(NodeLanguageVO nodeLanguageVO);
	
	List<NodeLanguageVO> getNodesByLanguage(String language);
	
	public void addLanguage(LanguageVO vo);
	
	public List<LanguageVO> getAllLanguage();

	List<NodeLanguageVO> getNodeLanguageById(String id);
	
}
