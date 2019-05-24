package org.occideas.mapper;

import org.occideas.entity.Language;
import org.occideas.vo.LanguageVO;

import java.util.List;

public interface LanguageMapper {

  Language convertToLanguage(LanguageVO vo);

  List<Language> convertToLanguageList(List<LanguageVO> voList);

  LanguageVO convertToLanguageVO(Language entity);

  List<LanguageVO> convertToLanguageVOList(List<Language> entityList);

}
