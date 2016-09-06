package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.ModuleFragment;
import org.occideas.vo.ModuleFragmentVO;
import org.springframework.stereotype.Component;

@Component
public class ModuleFragmentMapperImpl implements ModuleFragmentMapper{

	@Override
	public ModuleFragmentVO convertToModuleFragmentVO(ModuleFragment entity) {
		if(entity == null){
			return null;
		}
		ModuleFragmentVO vo = new ModuleFragmentVO();
		vo.setFragmentId(entity.getFragmentId());
		vo.setFragmentName(entity.getFragmentName());
		vo.setIdNode(entity.getIdNode());
		vo.setModuleId(entity.getModuleId());
		vo.setModuleName(entity.getModuleName());
		vo.setNodeNumber(entity.getNodeNumber());
		vo.setPrimaryKey(entity.getPrimaryKey());
		return vo;
	}

	@Override
	public List<ModuleFragmentVO> convertToModuleFragmentList(List<ModuleFragment> list) {
		if(list == null){
			return null;
		}
		
		List<ModuleFragmentVO> results = new ArrayList<>();
		for(ModuleFragment entity:list){
			results.add(convertToModuleFragmentVO(entity));
		}
		return results;
	}

}
