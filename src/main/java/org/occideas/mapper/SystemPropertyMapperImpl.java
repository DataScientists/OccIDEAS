package org.occideas.mapper;

import org.occideas.entity.SystemProperty;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SystemPropertyMapperImpl implements SystemPropertyMapper {

  @Override
  public SystemProperty convertSytemPropertyVOtoSystemProperty(SystemPropertyVO vo) {
    if (vo == null) {
      return null;
    }
    SystemProperty entity = new SystemProperty();
    entity.setId(vo.getId());
    entity.setName(vo.getName());
    entity.setType(vo.getType());
    entity.setUpdatedBy(vo.getUpdatedBy());
    entity.setValue(vo.getValue());
    return entity;
  }

  @Override
  public SystemPropertyVO convertSytemPropertyToSystemPropertyVO(SystemProperty entity) {
    if (entity == null) {
      return null;
    }
    SystemPropertyVO vo = new SystemPropertyVO();
    vo.setId(entity.getId());
    vo.setName(entity.getName());
    vo.setType(entity.getType());
    vo.setUpdatedBy(entity.getUpdatedBy());
    vo.setValue(entity.getValue());
    return vo;
  }

  @Override
  public List<SystemProperty> convertSystemPropertyVOListToSystemPropertyList(List<SystemPropertyVO> list) {
    if (list == null) {
      return null;
    }
    List<SystemProperty> result = new ArrayList<>();
    for (SystemPropertyVO vo : list) {
      result.add(convertSytemPropertyVOtoSystemProperty(vo));
    }

    return result;
  }

  @Override
  public List<SystemPropertyVO> convertSystemPropertyListToSystemPropertyVOList(List<SystemProperty> list) {
    if (list == null) {
      return null;
    }
    List<SystemPropertyVO> result = new ArrayList<>();
    for (SystemProperty entity : list) {
      result.add(convertSytemPropertyToSystemPropertyVO(entity));
    }
    return result;
  }

}
