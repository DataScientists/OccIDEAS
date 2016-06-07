package org.occideas.participant.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.ParticipantVO;

public interface ParticipantService extends BaseService<ParticipantVO> {

	List<ParticipantVO> findByIdForInterview(Long id);

	 public List<ParticipantVO> listAllParticipantWithInt();
	
}
