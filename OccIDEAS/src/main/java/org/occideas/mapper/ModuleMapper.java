package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.Module;
import org.occideas.vo.ModuleVO;


public class ModuleMapper {

	public ModuleVO convertToVO(Module module) {
		if (module == null) {
			return null;
		}
		ModuleVO moduleVO = new ModuleVO();
//		moduleVO.setChildNodes(module.getChildNodes());
		moduleVO.setDeleted(module.getDeleted());
		moduleVO.setDescription(module.getDescription());
		moduleVO.setIdNode(module.getIdNode());
		moduleVO.setLink(module.getLink());
		moduleVO.setName(module.getName());
		moduleVO.setNodeclass(module.getNodeclass());
		moduleVO.setNotes(module.getNotes());
		moduleVO.setNumber(module.getNumber());
		moduleVO.setOriginalId(module.getOriginalId());
		moduleVO.setParent(module.getParent());
		moduleVO.setSequence(module.getSequence());
		moduleVO.setTopNodeId(module.getTopNodeId());
		moduleVO.setType(module.getType());
		return moduleVO;
	}

	public List<ModuleVO> convertToListVO(List<Module> moduleList) {
		if (moduleList == null) {
			return null;
		}

		List<ModuleVO> list = new ArrayList<ModuleVO>();
		for (Module module : moduleList) {
			list.add(convertToVO(module));
		}
		return list;
	}


}
