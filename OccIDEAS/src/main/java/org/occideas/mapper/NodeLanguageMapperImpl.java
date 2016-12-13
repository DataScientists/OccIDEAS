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
public class NodeLanguageMapperImpl implements NodeLanguageMapper {

	@Autowired
	private LanguageMapper mapper;

	@Override
	public NodeLanguageVO convertToNodeLanguageVO(NodeLanguage entity) {
		if (entity == null) {
			return null;
		}

		NodeLanguageVO vo = new NodeLanguageVO();
		vo.setId(entity.getId());
		vo.setWord(entity.getWord());
		vo.setLanguage(mapper.convertToLanguageVO(entity.getLanguage()));
		vo.setLastUpdated(entity.getLastUpdated());
		vo.setTranslation(entity.getTranslation());
		vo.setLanguageId(entity.getLanguageId());
		return vo;
	}

	@Override
	public List<NodeLanguageVO> convertToNodeLanguageVOList(List<NodeLanguage> entityList) {
		if (entityList == null) {
			return null;
		}

		List<NodeLanguageVO> list = new ArrayList<>();
		for (NodeLanguage entity : entityList) {
			list.add(convertToNodeLanguageVO(entity));
		}

		return list;
	}

	@Override
	public NodeLanguage convertToNodeLanguage(NodeLanguageVO vo) {
		if (vo == null) {
			return null;
		}

		NodeLanguage entity = new NodeLanguage();
		if (vo.getId() != null) {
			entity.setId(vo.getId());
		}
		entity.setWord(vo.getWord());
		if (vo.getLanguage() != null) {
			entity.setLanguage(mapper.convertToLanguage(vo.getLanguage()));
		}
		if (vo.getLastUpdated() != null) {
			entity.setLastUpdated(vo.getLastUpdated());
		}
		entity.setTranslation(vo.getTranslation());
		entity.setLanguageId(vo.getLanguageId());
		return entity;
	}

	@Override
	public List<NodeLanguage> convertToNodeLanguageList(List<NodeLanguageVO> voList) {
		if (voList == null) {
			return null;
		}

		List<NodeLanguage> list = new ArrayList<>();
		for (NodeLanguageVO vo : voList) {
			list.add(convertToNodeLanguage(vo));
		}

		return list;
	}

	@Override
	public Language convertToLanguage(LanguageVO vo) {
		if (vo == null) {
			return null;
		}
		Language entity = new Language();
		entity.setDescription(vo.getDescription());
		entity.setLanguage(vo.getLanguage());
		entity.setFlag(vo.getFlag());
		return entity;
	}

	@Override
	public List<Language> convertToListLanguage(List<LanguageVO> voList) {
		if (voList == null) {
			return null;
		}
		List<Language> list = new ArrayList<>();
		for (LanguageVO vo : voList) {
			list.add(convertToLanguage(vo));
		}
		return list;
	}

	@Override
	public LanguageVO convertToLanguageVO(Language entity) {
		if (entity == null) {
			return null;
		}
		LanguageVO vo = new LanguageVO();
		vo.setId(entity.getId());
		vo.setDescription(entity.getDescription());
		vo.setLanguage(entity.getLanguage());
		vo.setFlag(entity.getFlag());
		return vo;
	}

	@Override
	public List<LanguageVO> convertToListLanguageVO(List<Language> entityList) {
		if (entityList == null) {
			return null;
		}
		List<LanguageVO> list = new ArrayList<>();
		for (Language entity : entityList) {
			list.add(convertToLanguageVO(entity));
		}
		return list;
	}

}
