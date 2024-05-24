package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.ParticipantDetails;
import org.occideas.vo.ParticipantDetailsVO;
import org.springframework.stereotype.Component;

@Component
public class ParticipantDetailsMapperImpl implements ParticipantDetailsMapper {

  @Override
  public ParticipantDetailsVO convertToParticipantDetailsVO(ParticipantDetails details) {
    if (details == null) {
      return null;
    }
    ParticipantDetailsVO detailsVO = new ParticipantDetailsVO();
    detailsVO.setId(details.getId());
    detailsVO.setDetailName(details.getDetailName());
    detailsVO.setDetailValue(details.getDetailValue());
    detailsVO.setParticipantId(details.getParticipantId());
    
    return detailsVO;
  }

  @Override
  public List<ParticipantDetailsVO> convertToParticipantDetailsVOList(List<ParticipantDetails> participantEntity) {
    if (participantEntity == null) {
      return null;
    }
    List<ParticipantDetailsVO> list = new ArrayList<ParticipantDetailsVO>();
    for (ParticipantDetails participant : participantEntity) {
      list.add(convertToParticipantDetailsVO(participant));
    }
    return list;
  }

  @Override
  public ParticipantDetails convertToParticipantDetails(ParticipantDetailsVO detailsVO) {
    if (detailsVO == null) {
      return null;
    }
    ParticipantDetails details = new ParticipantDetails();
    details.setId(detailsVO.getId());
    details.setDetailName(detailsVO.getDetailName());
    details.setDetailValue(detailsVO.getDetailValue());
    details.setParticipantId(detailsVO.getParticipantId());

    return details;
  }

  @Override
  public List<ParticipantDetails> convertToParticipantDetailsList(List<ParticipantDetailsVO> detailsVOs) {
    if (detailsVOs == null) {
      return null;
    }
    List<ParticipantDetails> list = new ArrayList<ParticipantDetails>();
    for (ParticipantDetailsVO details : detailsVOs) {
      
        list.add(convertToParticipantDetails(details));
      
    }
    return list;
  }

}
