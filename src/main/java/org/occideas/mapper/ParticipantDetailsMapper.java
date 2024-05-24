package org.occideas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.occideas.entity.ParticipantDetails;
import org.occideas.vo.ParticipantDetailsVO;

@Mapper(componentModel = "spring")
public interface ParticipantDetailsMapper {

  ParticipantDetailsVO convertToParticipantDetailsVO(ParticipantDetails participantDetails);

  List<ParticipantDetailsVO> convertToParticipantDetailsVOList(List<ParticipantDetails> participantDetails);

  ParticipantDetails convertToParticipantDetails(ParticipantDetailsVO participantDetailsVO);

  List<ParticipantDetails> convertToParticipantDetailsList(List<ParticipantDetailsVO> participantDetailsVO);

}
