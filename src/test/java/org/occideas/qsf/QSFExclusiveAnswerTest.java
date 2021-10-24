package org.occideas.qsf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QSFExclusiveAnswerTest {

    @Test
    void givenAnswer_whenIsAnswerExclusive_shouldReturnBoolean() {
        assertTrue(QSFExclusiveAnswer.isAnswerExclusive("No"));
        assertTrue(QSFExclusiveAnswer.isAnswerExclusive("Don't know"));
        assertTrue(QSFExclusiveAnswer.isAnswerExclusive("None of the Above"));
        assertFalse(QSFExclusiveAnswer.isAnswerExclusive("Yes"));
    }
}