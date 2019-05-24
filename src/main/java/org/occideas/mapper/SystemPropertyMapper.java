package org.occideas.mapper;

import org.occideas.entity.SystemProperty;
import org.occideas.vo.SystemPropertyVO;

import java.util.List;

public interface SystemPropertyMapper {

  SystemProperty convertSytemPropertyVOtoSystemProperty(SystemPropertyVO vo);

  SystemPropertyVO convertSytemPropertyToSystemPropertyVO(SystemProperty entity);

  List<SystemProperty> convertSystemPropertyVOListToSystemPropertyList(List<SystemPropertyVO> list);

  List<SystemPropertyVO> convertSystemPropertyListToSystemPropertyVOList(List<SystemProperty> list);
}
