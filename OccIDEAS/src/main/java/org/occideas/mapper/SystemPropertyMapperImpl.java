package org.occideas.mapper;

import org.occideas.entity.SystemProperty;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.stereotype.Component;

@Component
public class SystemPropertyMapperImpl implements SystemPropertyMapper{

	@Override
	public SystemProperty convertSytemPropertyVOtoSystemProperty(SystemPropertyVO vo) {
		if(vo == null){
			return null;
		}
		SystemProperty entity = new SystemProperty();
		entity.setSetBy(vo.getSetBy());
		entity.setValue(vo.getValue());
		entity.setVariable(vo.getVariable());
		return entity;
	}

	@Override
	public SystemPropertyVO convertSytemPropertyToSystemPropertyVO(SystemProperty entity) {
		if(entity == null){
			return null;
		}
		SystemPropertyVO vo = new SystemPropertyVO();
		vo.setSetBy(entity.getSetBy());
		vo.setValue(entity.getValue());
		vo.setVariable(entity.getVariable());
		return vo;
	}

}
