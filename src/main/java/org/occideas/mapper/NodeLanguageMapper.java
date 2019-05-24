package org.occideas.mapper;

import org.occideas.entity.Language;
import org.occideas.entity.NodeLanguage;
import org.occideas.vo.LanguageVO;
import org.occideas.vo.NodeLanguageVO;

import java.util.List;

public interface NodeLanguageMapper {

  NodeLanguageVO convertToNodeLanguageVO(NodeLanguage entity);

  List<NodeLanguageVO> convertToNodeLanguageVOList(List<NodeLanguage> entity);

  NodeLanguage convertToNodeLanguage(NodeLanguageVO vo);

  List<NodeLanguage> convertToNodeLanguageList(List<NodeLanguageVO> vo);

  Language convertToLanguage(LanguageVO vo);

  List<Language> convertToListLanguage(List<LanguageVO> voList);

  LanguageVO convertToLanguageVO(Language entity);

  List<LanguageVO> convertToListLanguageVO(List<Language> entityList);

  NodeLanguageVO convertToNodeLanguageVOOnly(NodeLanguage entity);

  List<NodeLanguageVO> convertToNodeLanguageVOListOnly(List<NodeLanguage> entityList);
}
