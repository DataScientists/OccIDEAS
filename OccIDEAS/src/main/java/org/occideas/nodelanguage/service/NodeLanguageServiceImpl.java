package org.occideas.nodelanguage.service;

import java.util.List;

import org.occideas.mapper.NodeLanguageMapper;
import org.occideas.nodelanguage.dao.NodeLanguageDao;
import org.occideas.vo.NodeLanguageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NodeLanguageServiceImpl implements NodeLanguageService{

	@Autowired
	private NodeLanguageDao dao;
	@Autowired
	private NodeLanguageMapper mapper;
	
	@Override
	public void save(NodeLanguageVO nodeLanguageVO) {
		dao.save(mapper.convertToNodeLanguage(nodeLanguageVO));
	}

	@Override
	public List<NodeLanguageVO> getNodesByLanguage(String language) {
		return mapper.convertToNodeLanguageVOList(dao.getNodesByLanguage(language));
	}

}