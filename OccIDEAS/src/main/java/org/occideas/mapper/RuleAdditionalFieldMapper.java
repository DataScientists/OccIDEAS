package org.occideas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.occideas.entity.RuleAdditionalField;
import org.occideas.vo.RuleAdditionalFieldVO;

@Mapper(componentModel = "spring")
public interface RuleAdditionalFieldMapper {

	RuleAdditionalFieldVO convertToRuleAdditionalFieldVO(RuleAdditionalField additionalField);

	List<RuleAdditionalFieldVO> convertToRuleAdditionalFieldVOList(List<RuleAdditionalField> additionalFieldList);

	RuleAdditionalField convertToRuleAdditionalField(RuleAdditionalFieldVO additionalFieldVO);
	
	List<RuleAdditionalField> convertToRuleAdditionalFieldList(List<RuleAdditionalFieldVO> additionalFieldVO);

	RuleAdditionalField convertToRuleAdditionalFieldWithId(RuleAdditionalFieldVO vo);

}
