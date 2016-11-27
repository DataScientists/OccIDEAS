package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.NodeLanguage;
import org.occideas.vo.NodeLanguageVO;

public interface NodeLanguageMapper {

	NodeLanguageVO convertToNodeLanguageVO(NodeLanguage entity);
	
	List<NodeLanguageVO> convertToNodeLanguageVOList(List<NodeLanguage> entity);
	
	NodeLanguage convertToNodeLanguage(NodeLanguageVO vo);
	
	List<NodeLanguage> convertToNodeLanguageList(List<NodeLanguageVO> vo);
}
