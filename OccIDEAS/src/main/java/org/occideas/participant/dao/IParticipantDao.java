package org.occideas.participant.dao;

import java.math.BigInteger;
import java.util.List;

import org.occideas.entity.AssessmentIntMod;
import org.occideas.entity.Interview;
import org.occideas.entity.Participant;
import org.occideas.entity.ParticipantIntMod;
import org.occideas.vo.GenericFilterVO;

public interface IParticipantDao {

	Long save(Participant participant);

	String getMaxReferenceNumber();

	Long getMaxParticipantId();

	BigInteger getParticipantWithModTotalCount(GenericFilterVO filter);

	List<ParticipantIntMod> getPaginatedParticipantWithModList(int pageNumber, int size, GenericFilterVO filter);

	BigInteger getPaginatedParticipantTotalCount(GenericFilterVO filter);

	List<ParticipantIntMod> getPaginatedParticipantList(int pageNumber, int size, GenericFilterVO filter);

	List<Participant> getAll();

	void saveOrUpdate(Participant participant);

	Participant merge(Interview participant);

	Participant get(Long id);

	void delete(Participant participant);

	List<AssessmentIntMod> getPaginatedAssessmentWithModList(int pageNumber, int size, GenericFilterVO filter);

	BigInteger getAsssessmentWithModTotalCount(GenericFilterVO filter);

	Participant getByReferenceNumber(String referenceNumber);

}
