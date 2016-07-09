package org.occideas.mapper;

import org.occideas.entity.GenericNode;
import org.occideas.vo.GenericNodeVO;
import org.springframework.stereotype.Component;

@Component
public class GenericNodeMapperImpl implements GenericNodeMapper{

	@Override
	public GenericNodeVO convertToGenNodeVO(GenericNode entity) {
		if(entity == null){
			return null;
		}
		GenericNodeVO vo = new GenericNodeVO();
		vo.setDeleted(entity.getDeleted());
		vo.setDescription(entity.getDescription());
		vo.setIdNode(entity.getIdNode());
		vo.setLastUpdated(entity.getLastUpdated());
		vo.setLink(entity.getLink());
		vo.setName(entity.getName());
		vo.setNodeclass(entity.getNodeclass());
		vo.setNumber(entity.getNumber());
		vo.setOriginalId(entity.getOriginalId());
		vo.setParentId(entity.getParentId());
		vo.setSequence(entity.getSequence());
		vo.setTopNodeId(entity.getTopNodeId());
		vo.setType(entity.getType());
		return vo;
	}

}
