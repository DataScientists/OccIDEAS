package org.occideas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.occideas.entity.AdditionalField;
import org.occideas.vo.AdditionalFieldVO;

@Mapper(componentModel = "spring")
public interface AdditionalFieldMapper {

	AdditionalFieldVO convertToAdditionalFieldVO(AdditionalField additionalField);

	List<AdditionalFieldVO> convertToAdditionalFieldVOList(List<AdditionalField> additionalFieldList);

	AdditionalField convertToAdditionalField(AdditionalFieldVO additionalFieldVO);
	
	List<AdditionalField> convertToAdditionalFieldList(List<AdditionalFieldVO> additionalFieldVO);
	
}
