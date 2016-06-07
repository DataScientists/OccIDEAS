package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Interview;
import org.occideas.entity.Participant;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.ParticipantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParticipantMapperImpl implements ParticipantMapper {

	@Autowired
	private InterviewMapper interviewMapper;

	@Override
	public ParticipantVO convertToParticipantVO(Participant participant, boolean includeInterviews) {
		if (participant == null) {
			return null;
		}
		ParticipantVO participantVO = new ParticipantVO();
		participantVO.setIdParticipant(participant.getIdParticipant());
		participantVO.setReference(participant.getReference());
		participantVO.setStatus(participant.getStatus());
		participantVO.setDeleted(participant.getDeleted());
		List<Interview> interviews = participant.getInterviews();
		if (includeInterviews && !CommonUtil.isListEmpty(interviews)) {
			participantVO.setInterviews(interviewMapper.convertToInterviewVOList(interviews));
		}

		return participantVO;
	}

	@Override
	public ParticipantVO convertToInterviewParticipantVO(Participant participant) {
		if (participant == null) {
			return null;
		}
		ParticipantVO participantVO = new ParticipantVO();
		participantVO.setIdParticipant(participant.getIdParticipant());
		participantVO.setReference(participant.getReference());
		participantVO.setStatus(participant.getStatus());
		participantVO.setDeleted(participant.getDeleted());

		participantVO.setInterviews(interviewMapper.convertToInterviewVOList(participant.getInterviews()));

		return participantVO;
	}

	@Override
	public List<ParticipantVO> convertToParticipantVOList(List<Participant> participantEntity,
			boolean includeInterviews) {
		if (participantEntity == null) {
			return null;
		}
		List<ParticipantVO> list = new ArrayList<ParticipantVO>();
		for (Participant participant : participantEntity) {
			list.add(convertToParticipantVO(participant, includeInterviews));
		}
		return list;
	}

	@Override
	public Participant convertToParticipant(ParticipantVO participantVO, boolean includeInterviews) {
		if (participantVO == null) {
			return null;
		}
		Participant participant = new Participant();

		participant.setIdParticipant(participantVO.getIdParticipant());
		participant.setReference(participantVO.getReference());
		participant.setStatus(participantVO.getStatus());
		participant.setDeleted(participantVO.getDeleted());
		if (includeInterviews) {
			participant.setInterviews(interviewMapper.convertToInterviewList(participantVO.getInterviews()));
		}
		return participant;
	}

	@Override
	public List<Participant> convertToParticipantList(List<ParticipantVO> participantVO, boolean includeInterviews) {
		if (participantVO == null) {
			return null;
		}

		List<Participant> list = new ArrayList<Participant>();
		for (ParticipantVO participantVO_ : participantVO) {
			list.add(convertToParticipant(participantVO_, includeInterviews));
		}

		return list;
	}

	@Override
	public List<ParticipantVO> convertToParticipantVOListOnly(List<Participant> participantEntity) {
		if (participantEntity == null) {
			return null;
		}
		List<ParticipantVO> list = new ArrayList<ParticipantVO>();
		for (Participant participant : participantEntity) {
			list.add(convertToParticipantVOonly(participant));
		}
		return list;
	}

	@Override
	public ParticipantVO convertToParticipantVOonly(Participant participant) {
		if (participant == null) {
			return null;
		}
		ParticipantVO participantVO = new ParticipantVO();
		participantVO.setIdParticipant(participant.getIdParticipant());
		participantVO.setReference(participant.getReference());
		participantVO.setStatus(participant.getStatus());
		participantVO.setDeleted(participant.getDeleted());

		participantVO.setInterviews(interviewMapper.convertToInterviewVOnoQsList(participant.getInterviews()));

		return participantVO;
	}

}
