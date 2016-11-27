package org.occideas.nodelanguage.service;

import java.util.List;

import org.occideas.vo.NodeLanguageVO;

public interface NodeLanguageService {

	void save(NodeLanguageVO nodeLanguageVO);
	
	List<NodeLanguageVO> getNodesByLanguage(String language);
	
	
}
