package org.occideas.mapper;

import org.occideas.entity.Translate;
import org.occideas.vo.TranslateVO;

import java.util.List;

public interface TranslateMapper {

  Translate convertToTranslate(TranslateVO vo);

  List<Translate> convertToTranslateList(List<TranslateVO> voList);

  TranslateVO convertToTranslateVO(Translate entity);

  List<TranslateVO> convertToTranslateVOList(List<Translate> entityList);

}
