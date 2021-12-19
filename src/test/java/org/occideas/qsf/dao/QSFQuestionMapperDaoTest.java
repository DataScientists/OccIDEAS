package org.occideas.qsf.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.entity.QSFQuestionMapper;
import org.occideas.entity.QSFQuestionMapperId;
import org.occideas.qsf.QSFNodeTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class QSFQuestionMapperDaoTest {

    @Autowired
    QSFQuestionMapperDao qsfQuestionMapperDao;

    @AfterEach
    public void cleanup() {
        qsfQuestionMapperDao.deleteAll();
    }

    @Test
    public void givenSurveyId_whenGetQuestionBySurveyId_thenReturnRecords() {
        String s12345 = "s12345";
        qsfQuestionMapperDao.save(new QSFQuestionMapper(new QSFQuestionMapperId(s12345, "QID1"), 1L,
                QSFNodeTypeMapper.Q_MULTIPLE.getDescription(), "Test Question 1", null));
        qsfQuestionMapperDao.save(new QSFQuestionMapper(new QSFQuestionMapperId(s12345, "QID2"), 2L,
                QSFNodeTypeMapper.Q_MULTIPLE.getDescription(), "Test Question 2", null));

        Map<String, QSFQuestionMapper> results = qsfQuestionMapperDao.getQuestionBySurveyId(s12345);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(2, results.size());
        assertEquals(1L, results.get("QID1").getIdNode());
        assertEquals(2L, results.get("QID2").getIdNode());
    }

    @Test
    public void givenSurveyId_whenDeleteBySurveyId_thenDeleteRecords() {
        String s12345 = "s12345";
        qsfQuestionMapperDao.save(new QSFQuestionMapper(new QSFQuestionMapperId(s12345, "QID1"), 1L,
                QSFNodeTypeMapper.Q_MULTIPLE.getDescription(), "Test Question 1", null));
        qsfQuestionMapperDao.save(new QSFQuestionMapper(new QSFQuestionMapperId(s12345, "QID2"), 2L,
                QSFNodeTypeMapper.Q_MULTIPLE.getDescription(), "Test Question 2", null));

        qsfQuestionMapperDao.deleteBySurveyId(s12345);
        Map<String, QSFQuestionMapper> results = qsfQuestionMapperDao.getQuestionBySurveyId(s12345);

        assertTrue(results.isEmpty());

    }

}