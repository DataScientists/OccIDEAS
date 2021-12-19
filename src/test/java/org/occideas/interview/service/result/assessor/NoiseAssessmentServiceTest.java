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
                new BigDecimal(8.083333333333334),
                new BigDecimal(4.083333333333334));

        assertEquals(new BigDecimal(4.0000).setScale(4, RoundingMode.HALF_UP), actual.setScale(4, RoundingMode.HALF_UP));
    }

    @Test
    void givenLevelAndBackgroundHours_whenDerivePartialExposure_shouldReturnCorrectValue() {
        BigDecimal actual = noiseAssessmentService.derivePartialExposure(new BigDecimal(65),
                new BigDecimal(4.0000).setScale(4, RoundingMode.HALF_UP));

        assertEquals(new BigDecimal(0.00509).setScale(4, RoundingMode.HALF_UP), actual.setScale(4, RoundingMode.HALF_UP));
    }

    @Test
    void givenTotalPartialExposure_whenDeriveAutoExposure_shouldReturnCorrectValue() {
        BigDecimal actual = noiseAssessmentService.deriveAutoExposure(new BigDecimal(3.2640).setScale(4, RoundingMode.HALF_UP));

        assertEquals(new BigDecimal(90.0860).setScale(4, RoundingMode.HALF_UP), actual.setScale(4, RoundingMode.HALF_UP));
    }
}