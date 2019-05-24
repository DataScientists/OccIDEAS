package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.ModuleIntroModule;
import org.occideas.vo.ModuleIntroModuleVO;
import org.springframework.stereotype.Component;

@Component
public class ModuleIntroModuleMapperImpl implements ModuleIntroModuleMapper{

	@Override
	public ModuleIntroModuleVO convertToModuleIntroModuleVO(ModuleIntroModule entity) {
		if(entity == null){
			return null;
		}
		ModuleIntroModuleVO vo = new ModuleIntroModuleVO();
		vo.setIdNode(entity.getIdNode());
		vo.setModuleId(entity.getModuleId());
		vo.setModuleLinkId(entity.getModuleLinkId());
		vo.setModuleLinkName(entity.getModuleLinkName());
		vo.setModuleName(entity.getModuleName());
		vo.setNodeNumber(entity.getNodeNumber());
		vo.setPrimaryKey(entity.getPrimaryKey());
		return vo;
	}

	@Override
	public List<ModuleIntroModuleVO> convertToModuleIntroModuleList(List<ModuleIntroModule> list) {
		if(list == null){
			return null;
		}
		
		List<ModuleIntroModuleVO> voList = new ArrayList<>();
		for(ModuleIntroModule entity:list){
			voList.add(convertToModuleIntroModuleVO(entity));
		}
		return voList;
	}

	
	
	
}
