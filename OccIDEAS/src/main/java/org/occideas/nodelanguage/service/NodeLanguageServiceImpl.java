package org.occideas.nodelanguage.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.occideas.entity.Language;
import org.occideas.entity.Module;
import org.occideas.entity.NodeLanguage;
import org.occideas.entity.NodeNodeLanguageMod;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.NodeLanguageMapper;
import org.occideas.nodelanguage.dao.INodeLanguageDao;
import org.occideas.vo.LanguageVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeLanguageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NodeLanguageServiceImpl implements NodeLanguageService{

	@Autowired
	private INodeLanguageDao dao;
	@Autowired
	private NodeLanguageMapper mapper;
	@Autowired
	private ModuleMapper modMapper;
	
	@Override
	public void save(NodeLanguageVO nodeLanguageVO) {
		NodeLanguage nodeLanguage = dao.getNodeLanguageByWordAndLanguage(nodeLanguageVO.getWord(),nodeLanguageVO.getLanguageId());
		if(nodeLanguage != null){
			nodeLanguageVO.setId(nodeLanguage.getId());
		}
		if(nodeLanguage != null && nodeLanguage.getTranslation().equals(nodeLanguageVO.getTranslation())){
			return;
		}
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
	public List<NodeNodeLanguageMod> getNodeNodeLanguageListMod() {
		return dao.getNodeNodeLanguageListMod();
	}

	@Override
	public Integer getUntranslatedModules(String flag) {
		List<Module> untranslatedModules = dao.getUntranslatedModules(flag);
		if(untranslatedModules == null){
			return 0;
		}
		return untranslatedModules.size();
	}

	@Override
	public Integer getTotalUntranslatedModule() {
		return dao.getTotalUntranslatedModule();
	}

	@Override
	public Integer getTotalModuleCount() {
		return dao.getTotalModuleCount();
	}
}
