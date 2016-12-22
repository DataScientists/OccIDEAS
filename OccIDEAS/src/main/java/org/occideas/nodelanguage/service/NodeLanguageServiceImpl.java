package org.occideas.nodelanguage.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.occideas.entity.Language;
import org.occideas.entity.NodeNodeLanguage;
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
		if(distinctNodeLanguageId.isEmpty()){
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
	public List<NodeNodeLanguage> getNodeNodeLanguageList() {
		return dao.getNodeNodeLanguageList();
	}
}
