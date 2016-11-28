package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Language;
import org.occideas.entity.NodeLanguage;
import org.occideas.vo.LanguageVO;
import org.occideas.vo.NodeLanguageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeLanguageMapperImpl implements NodeLanguageMapper{

	@Autowired
	private LanguageMapper mapper;
	
	@Override
	public NodeLanguageVO convertToNodeLanguageVO(NodeLanguage entity) {
		if(entity == null){
			return null;
		}
		
		NodeLanguageVO vo = new NodeLanguageVO();
		vo.setDefault(entity.isDefault());
		vo.setId(entity.getId());
		vo.setKey(entity.getKey());
		vo.setLanguage(mapper.convertToLanguageVO(entity.getLanguage()));
		vo.setLastUpdated(entity.getLastUpdated());
		vo.setValue(entity.getValue());
		return vo;
	}

	@Override
	public List<NodeLanguageVO> convertToNodeLanguageVOList(List<NodeLanguage> entityList) {
		if(entityList == null){
			return null;
		}
		
		List<NodeLanguageVO> list = new ArrayList<>();
		for(NodeLanguage entity:entityList){
			list.add(convertToNodeLanguageVO(entity));
		}
		
		return list;
	}

	@Override
	public NodeLanguage convertToNodeLanguage(NodeLanguageVO vo) {
		if(vo == null){
			return null;
		}
		
		NodeLanguage entity = new NodeLanguage();
		entity.setDefault(vo.isDefault());
		entity.setId(vo.getId());
		entity.setKey(vo.getKey());
		entity.setLanguage(mapper.convertToLanguage(vo.getLanguage()));
		entity.setLastUpdated(vo.getLastUpdated());
		entity.setValue(vo.getValue());
		
		return entity;
	}

	@Override
	public List<NodeLanguage> convertToNodeLanguageList(List<NodeLanguageVO> voList) {
		if(voList == null){
			return null;
		}
		
		List<NodeLanguage> list = new ArrayList<>();
		for(NodeLanguageVO vo:voList){
			list.add(convertToNodeLanguage(vo));
		}
		
		return list;
	}

	@Override
	public Language convertToLanguage(LanguageVO vo) {
		if(vo == null){
			return null;
		}
		Language entity = new Language();
		entity.setDescription(vo.getDescription());
		entity.setLanguage(vo.getLanguage());
		return entity;
	}

	@Override
	public List<Language> convertToListLanguage(List<LanguageVO> voList) {
		if(voList == null){
			return null;
		}
		List<Language> list = new ArrayList<>();
		for(LanguageVO vo:voList){
			list.add(convertToLanguage(vo));
		}
		return list;
	}

	@Override
	public LanguageVO convertToLanguageVO(Language entity) {
		if(entity == null){
			return null;
		}
		LanguageVO vo = new  LanguageVO();
		vo.setDescription(entity.getDescription());
		vo.setLanguage(entity.getLanguage());
		return vo;
	}

	@Override
	public List<LanguageVO> convertToListLanguageVO(List<Language> entityList) {
		if(entityList == null){
			return null;
		}
		List<LanguageVO> list = new ArrayList<>();
		for(Language entity:entityList){
			list.add(convertToLanguageVO(entity));
		}
		return list;
	}

}
