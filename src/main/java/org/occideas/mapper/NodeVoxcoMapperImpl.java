package org.occideas.mapper;

import org.occideas.entity.NodeVoxco;
import org.occideas.vo.NodeVoxcoVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NodeVoxcoMapperImpl implements NodeVoxcoMapper {

    @Override
    public NodeVoxcoVO convertToNodeVoxcoVO(NodeVoxco entity) {
        if (entity == null) return null;

        NodeVoxcoVO vo = new NodeVoxcoVO();
        vo.setSurveyId(entity.getSurveyId());
        vo.setIdNode(entity.getIdNode());
        vo.setSurveyName(entity.getSurveyName());
        vo.setImportFilterCount(entity.getImportFilterCount());
        vo.setImportQuestionCount(entity.getImportQuestionCount());
        vo.setVoxcoQuestionCount(entity.getVoxcoQuestionCount());
        vo.setLastValidated(entity.getLastValidated());
        return vo;
    }

    @Override
    public List<NodeVoxcoVO> convertToNodeVoxcoVOList(List<NodeVoxco> entities) {
        if (entities == null || entities.isEmpty()) return null;

        List<NodeVoxcoVO> voList = new ArrayList<>();
        entities.stream().forEach(entity -> {
            voList.add(convertToNodeVoxcoVO(entity));
        });
        return voList;
    }
}
