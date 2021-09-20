package org.occideas.interview.service.result.assessor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class NoiseAssessmentServiceTest {

    private NoiseAssessmentService noiseAssessmentService = new NoiseAssessmentService();

    @Test
    void givenTotalFrequency_whenDeriveBackgroundHours_shouldReturnCorrectValue() {
        BigDecimal actual = noiseAssessmentService.deriveBackgroundHours(
                new BigDecimal(10.5),
                new BigDecimal(5.583333333333334));

        assertEquals(new BigDecimal(4.9167).setScale(4, RoundingMode.UP), actual);
    }

    @Test
    void givenLevelAndBackgroundHours_whenDerivePartialExposure_shouldReturnCorrectValue() {
        BigDecimal actual = noiseAssessmentService.derivePartialExposure(new BigDecimal(65),
                new BigDecimal(4.9167).setScale(4, RoundingMode.UP));

        assertEquals(new BigDecimal(0.0062191882286999483).setScale(19, RoundingMode.FLOOR), actual);
    }

    void givenTotalPartialExposure_whenDeriveAutoExposure_shouldReturnCorrectValue() {
        BigDecimal actual = noiseAssessmentService.deriveAutoExposure(new BigDecimal(3.3254).setScale(4, RoundingMode.FLOOR));

        assertEquals(new BigDecimal(90.1700).setScale(4, RoundingMode.FLOOR), actual);
    }
}