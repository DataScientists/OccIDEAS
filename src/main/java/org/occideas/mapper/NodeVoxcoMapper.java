package org.occideas.mapper;

import org.occideas.entity.NodeVoxco;
import org.occideas.vo.NodeVoxcoVO;

import java.util.List;

public interface NodeVoxcoMapper {

    NodeVoxcoVO convertToNodeVoxcoVO(NodeVoxco entity);

    List<NodeVoxcoVO> convertToNodeVoxcoVOList(List<NodeVoxco> entities);

}
