package org.occideas.nodelanguage.service;

import java.util.List;

import javax.transaction.Transactional;

import org.occideas.mapper.NodeLanguageMapper;
import org.occideas.nodelanguage.dao.NodeLanguageDao;
import org.occideas.vo.LanguageVO;
import org.occideas.vo.NodeLanguageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
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
	
	@Override
	public void addLanguage(LanguageVO vo){
		dao.addLanguage(mapper.convertToLanguage(vo));
	}
	
	@Override
	public List<LanguageVO> getAllLanguage(){
		return mapper.convertToListLanguageVO(dao.getAllLanguage());
	}

	@Override
	public List<NodeLanguageVO> getNodeLanguageById(String id) {
		return mapper.convertToNodeLanguageVOList(dao.getNodeLanguageById(Long.valueOf(id)));
	}
	
	@Override
	public NodeLanguageVO getNodesByLanguageAndWord(Long languageId, String word){
		return mapper.convertToNodeLanguageVO(dao.getNodesByLanguageAndWord(languageId, word));
	}

	@Override
	public void delete(NodeLanguageVO vo) {
		dao.delete(mapper.convertToNodeLanguage(vo));
	}
}
