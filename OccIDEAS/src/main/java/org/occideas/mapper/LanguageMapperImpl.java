package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Language;
import org.occideas.vo.LanguageVO;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapperImpl implements LanguageMapper{

	@Override
	public Language convertToLanguage(LanguageVO vo) {
		if(vo == null){
			return null;
		}
		Language entity = new Language();
		entity.setDescription(vo.getDescription());
		entity.setId(vo.getId());
		entity.setLanguage(vo.getLanguage());
		entity.setLastUpdated(vo.getLastUpdated());
		entity.setFlag(vo.getFlag());
		return entity;
	}

	@Override
	public List<Language> convertToLanguageList(List<LanguageVO> voList) {
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
		LanguageVO vo = new LanguageVO();
		vo.setDescription(entity.getDescription());
		vo.setId(entity.getId());
		vo.setLanguage(entity.getLanguage());
		vo.setLastUpdated(entity.getLastUpdated());
		vo.setFlag(entity.getFlag());
		return vo;
	}

	@Override
	public List<LanguageVO> convertToLanguageVOList(List<Language> entityList) {
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
