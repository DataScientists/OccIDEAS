package org.occideas.assessment.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.entity.AssessmentAnswerSummary;
import org.occideas.entity.Constant;
import org.occideas.vo.AssessmentAnswerSummaryFilterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AssessmentDaoTest {

    @Autowired
    AssessmentDao assessmentDao;

    @Test
    void givenExistingData_whenGetAnswerSummaryByName_shouldReturnFilteredResults() {
        AssessmentAnswerSummaryFilterVO answerSummaryFilterVO = new AssessmentAnswerSummaryFilterVO();
        answerSummaryFilterVO.setName("Textile manufacturer (including yarn and thread, fabric or clothing)");
        answerSummaryFilterVO.setPageNumber(1);
        answerSummaryFilterVO.setSize(10);

        List<AssessmentAnswerSummary> actual = assessmentDao.getAnswerSummary(answerSummaryFilterVO);

        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void givenExistingData_whenGetAnswerSummaryByAnswerId_shouldReturnFilteredResults() {
        AssessmentAnswerSummaryFilterVO answerSummaryFilterVO = new AssessmentAnswerSummaryFilterVO();
        answerSummaryFilterVO.setAnswerId(67692L);
        answerSummaryFilterVO.setPageNumber(1);
        answerSummaryFilterVO.setSize(10);

        List<AssessmentAnswerSummary> actual = assessmentDao.getAnswerSummary(answerSummaryFilterVO);

        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void givenExistingData_whenGetAnswerSummaryByModuleName_shouldReturnFilteredResults() {
        AssessmentAnswerSummaryFilterVO answerSummaryFilterVO = new AssessmentAnswerSummaryFilterVO();
        answerSummaryFilterVO.setModuleName("NonExisting");
        answerSummaryFilterVO.setPageNumber(1);
        answerSummaryFilterVO.setSize(10);

        List<AssessmentAnswerSummary> actual = assessmentDao.getAnswerSummary(answerSummaryFilterVO);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void givenExistingData_whenGetAnswerSummaryByInterviewId_shouldReturnFilteredResults() {
        AssessmentAnswerSummaryFilterVO answerSummaryFilterVO = new AssessmentAnswerSummaryFilterVO();
        answerSummaryFilterVO.setIdinterview(1L);
        answerSummaryFilterVO.setPageNumber(1);
        answerSummaryFilterVO.setSize(10);

        List<AssessmentAnswerSummary> actual = assessmentDao.getAnswerSummary(answerSummaryFilterVO);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void givenExistingData_whenGetAnswerSummaryByParticipantId_shouldReturnFilteredResults() {
        AssessmentAnswerSummaryFilterVO answerSummaryFilterVO = new AssessmentAnswerSummaryFilterVO();
        answerSummaryFilterVO.setIdParticipant(2499L);
        answerSummaryFilterVO.setPageNumber(1);
        answerSummaryFilterVO.setSize(10);

        List<AssessmentAnswerSummary> actual = assessmentDao.getAnswerSummary(answerSummaryFilterVO);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void givenExistingData_whenGetAnswerSummaryByReference_shouldReturnFilteredResults() {
        AssessmentAnswerSummaryFilterVO answerSummaryFilterVO = new AssessmentAnswerSummaryFilterVO();
        answerSummaryFilterVO.setReference("Not Existing");
        answerSummaryFilterVO.setPageNumber(1);
        answerSummaryFilterVO.setSize(10);

        List<AssessmentAnswerSummary> actual = assessmentDao.getAnswerSummary(answerSummaryFilterVO);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void givenExistingData_whenGetAnswerSummaryByAssessedStatus_shouldReturnFilteredResults() {
        AssessmentAnswerSummaryFilterVO answerSummaryFilterVO = new AssessmentAnswerSummaryFilterVO();
        answerSummaryFilterVO.setReference(Constant.NOT_ASSESSED);
        answerSummaryFilterVO.setPageNumber(1);
        answerSummaryFilterVO.setSize(10);

        List<AssessmentAnswerSummary> actual = assessmentDao.getAnswerSummary(answerSummaryFilterVO);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void givenExistingData_whenGetAnswerSummaryByStatus_shouldReturnFilteredResults() {
        AssessmentAnswerSummaryFilterVO answerSummaryFilterVO = new AssessmentAnswerSummaryFilterVO();
        answerSummaryFilterVO.setStatusDescription("Running");
        answerSummaryFilterVO.setPageNumber(1);
        answerSummaryFilterVO.setSize(10);

        List<AssessmentAnswerSummary> actual = assessmentDao.getAnswerSummary(answerSummaryFilterVO);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void givenExistingData_whenGetAnswerSummaryNoFilter_shouldReturnAllResults() {
        AssessmentAnswerSummaryFilterVO answerSummaryFilterVO = new AssessmentAnswerSummaryFilterVO();
        answerSummaryFilterVO.setPageNumber(1);
        answerSummaryFilterVO.setSize(10);

        List<AssessmentAnswerSummary> actual = assessmentDao.getAnswerSummary(answerSummaryFilterVO);

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
    }

    @Test
    void givenExistingData_whenGetAnswerSummaryByNameTotalCount_shouldReturnCount() {
        AssessmentAnswerSummaryFilterVO answerSummaryFilterVO = new AssessmentAnswerSummaryFilterVO();
        answerSummaryFilterVO.setName("Textile manufacturer (including yarn and thread, fabric or clothing)");
        answerSummaryFilterVO.setPageNumber(1);
        answerSummaryFilterVO.setSize(10);

        Long count = assessmentDao.getAnswerSummaryByNameTotalCount(answerSummaryFilterVO);

        assertEquals(1, count);
    }
}