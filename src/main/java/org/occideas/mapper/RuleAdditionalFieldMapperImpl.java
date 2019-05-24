package org.occideas.mapper;

import org.occideas.entity.RuleAdditionalField;
import org.occideas.vo.RuleAdditionalFieldVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RuleAdditionalFieldMapperImpl implements RuleAdditionalFieldMapper {

  @Autowired
  private AdditionalFieldMapper additionalFieldMapper;

  @Override
  public RuleAdditionalFieldVO convertToRuleAdditionalFieldVO(RuleAdditionalField entity) {
    if (entity == null) {
      return null;
    }

    RuleAdditionalFieldVO vo = new RuleAdditionalFieldVO();
    vo.setIdRuleAdditionalField(entity.getIdRuleAdditionalField());
    vo.setAdditionalfield(additionalFieldMapper.convertToAdditionalFieldVO(entity.getAdditionalfield()));
    vo.setValue(entity.getValue());
    vo.setIdRule(entity.getIdRule());
    return vo;
  }

  @Override
  public List<RuleAdditionalFieldVO> convertToRuleAdditionalFieldVOList(List<RuleAdditionalField> moduleEntity) {
    if (moduleEntity == null) {
      return null;
    }

    List<RuleAdditionalFieldVO> list = new ArrayList<RuleAdditionalFieldVO>();
    for (RuleAdditionalField module : moduleEntity) {
      list.add(convertToRuleAdditionalFieldVO(module));
    }

    return list;
  }

  @Override
  public RuleAdditionalField convertToRuleAdditionalField(RuleAdditionalFieldVO vo) {
    if (vo == null) {
      return null;
    }
    RuleAdditionalField entity = new RuleAdditionalField();

    entity.setAdditionalfield(additionalFieldMapper.convertToAdditionalField(vo.getAdditionalfield()));
    entity.setValue(vo.getValue());
    entity.setIdRule(vo.getIdRule());
    return entity;
  }

  @Override
  public RuleAdditionalField convertToRuleAdditionalFieldWithId(RuleAdditionalFieldVO vo) {
    if (vo == null) {
      return null;
    }
    RuleAdditionalField entity = new RuleAdditionalField();

    entity.setAdditionalfield(additionalFieldMapper.convertToAdditionalField(vo.getAdditionalfield()));
    entity.setValue(vo.getValue());
    entity.setIdRuleAdditionalField(vo.getIdRuleAdditionalField());
    entity.setIdRule(vo.getIdRule());
    return entity;
  }

  @Override
  public List<RuleAdditionalField> convertToRuleAdditionalFieldList(List<RuleAdditionalFieldVO> voList) {
    if (voList == null) {
      return null;
    }

    List<RuleAdditionalField> list = new ArrayList<RuleAdditionalField>();
    for (RuleAdditionalFieldVO vo : voList) {
      if (vo.getIdRuleAdditionalField() != null) {
        list.add(convertToRuleAdditionalFieldWithId(vo));
      } else {
        list.add(convertToRuleAdditionalField(vo));
      }
    }

    return list;
  }

}

