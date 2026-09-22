package org.occideas.participant.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.entity.AssessmentIntMod;
import org.occideas.entity.ParticipantIntMod;
import org.occideas.vo.GenericFilterVO;
import org.occideas.vo.PageVO;
import org.occideas.vo.ParticipantVO;

public interface ParticipantService extends BaseService<ParticipantVO> {

  List<ParticipantVO> findByIdForInterview(Long id);

  List<ParticipantVO> listAllParticipantWithInt();

  PageVO<ParticipantIntMod> getPaginatedParticipantWithModList(int pageNumber, int size, GenericFilterVO filterVO);

  PageVO<ParticipantIntMod> getPaginatedParticipantList(int pageNumber, int size, GenericFilterVO filterVO);

  Long getMaxParticipantId();

  String getMaxReferenceNumber();

  void updateNewTransaction(ParticipantVO o);

  PageVO<AssessmentIntMod> getPaginatedAssessmentWithModList(int pageNumber, int size, GenericFilterVO filterVO);

  // Same as the inherited create(ParticipantVO) but, when auto-assigning a reference, applies the
  // startInterviewIdPrefix SYS_CONFIG prefix (see Constant.START_INTERVIEW_ID_PREFIX). Used only by
  // the public startInterview flow - every other creator (admin screens, CSV/Qualtrics/Voxco/IPSOS
  // imports) keeps using the plain create(ParticipantVO) and is unaffected by this config.
  ParticipantVO createPublic(ParticipantVO o);

  ParticipantVO getByReferenceNumber(String referenceNumber);
  
  List<ParticipantVO> getByReferenceNumberPrefix(String referenceNumberPrefix);

  Boolean checkIfStudyAgentPreLoaded();

  void softDeleteAll();
}
