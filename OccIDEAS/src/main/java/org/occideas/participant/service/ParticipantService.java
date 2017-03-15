package org.occideas.participant.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.entity.ParticipantIntMod;
import org.occideas.vo.GenericFilterVO;
import org.occideas.vo.PageVO;
import org.occideas.vo.ParticipantVO;

public interface ParticipantService extends BaseService<ParticipantVO> {

	List<ParticipantVO> findByIdForInterview(Long id);

	 public List<ParticipantVO> listAllParticipantWithInt();
	 
	 public PageVO<ParticipantIntMod> getPaginatedParticipantWithModList(int pageNumber,int size, GenericFilterVO filterVO);
	 
	 public PageVO<ParticipantIntMod> getPaginatedParticipantList(int pageNumber,int size, GenericFilterVO filterVO);

	Long getMaxParticipantId();

	  public String getMaxReferenceNumber();

	void updateNewTransaction(ParticipantVO o);
}
