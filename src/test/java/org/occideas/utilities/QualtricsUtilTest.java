package org.occideas.utilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@ExtendWith(MockitoExtension.class)
class QualtricsUtilTest {

    @Test
    void givenAnswerInStringFormat_whenParseAnswers_thenReturnListOfAnswers() {
        assertArrayEquals(new Long[]{69991L, 69996L}, QualtricsUtil.parseAnswers("[69991,69996]").toArray());
        assertArrayEquals(new Long[]{69991L}, QualtricsUtil.parseAnswers("69991").toArray());
        assertArrayEquals(new Long[]{69991L}, QualtricsUtil.parseAnswers("[69991]").toArray());
        assertArrayEquals(new Long[]{69996L}, QualtricsUtil.parseAnswers("        69996").toArray());
        assertArrayEquals(new Long[]{}, QualtricsUtil.parseAnswers("69991a").toArray());
    }

}