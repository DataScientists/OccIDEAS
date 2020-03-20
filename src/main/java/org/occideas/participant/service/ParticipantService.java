package org.occideas.participant.service;

import org.occideas.base.service.BaseService;
import org.occideas.entity.AssessmentIntMod;
import org.occideas.entity.ParticipantIntMod;
import org.occideas.vo.GenericFilterVO;
import org.occideas.vo.PageVO;
import org.occideas.vo.ParticipantVO;

import java.util.List;

public interface ParticipantService extends BaseService<ParticipantVO> {

  List<ParticipantVO> findByIdForInterview(Long id);

  List<ParticipantVO> listAllParticipantWithInt();

  PageVO<ParticipantIntMod> getPaginatedParticipantWithModList(int pageNumber, int size, GenericFilterVO filterVO);

  PageVO<ParticipantIntMod> getPaginatedParticipantList(int pageNumber, int size, GenericFilterVO filterVO);

  Long getMaxParticipantId();

  String getMaxReferenceNumber();

  void updateNewTransaction(ParticipantVO o);

  PageVO<AssessmentIntMod> getPaginatedAssessmentWithModList(int pageNumber, int size, GenericFilterVO filterVO);

  ParticipantVO getByReferenceNumber(String referenceNumber);

  Boolean checkIfStudyAgentPreLoaded();

  void softDeleteAll();
}
