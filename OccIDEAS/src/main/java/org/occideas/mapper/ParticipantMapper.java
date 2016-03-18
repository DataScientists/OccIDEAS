package org.occideas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.occideas.entity.Participant;
import org.occideas.vo.ParticipantVO;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

	
	List<ParticipantVO> convertToParticipantVOList(List<Participant> ParticipantList,boolean includeInterviews);

	Participant convertToParticipant(ParticipantVO ParticipantVO,boolean includeInterviews);
	
	List<Participant> convertToParticipantList(List<ParticipantVO> ParticipantVO,boolean includeInterviews);

	ParticipantVO convertToParticipantVO(Participant interview,boolean includeInterviews);
	
}
