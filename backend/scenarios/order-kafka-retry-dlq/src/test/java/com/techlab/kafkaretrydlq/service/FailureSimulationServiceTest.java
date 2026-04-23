package com.techlab.kafkaretrydlq.service;

import com.techlab.kafkaretrydlq.domain.FailureMode;
import com.techlab.kafkaretrydlq.domain.OrderCreatedEvent;
import com.techlab.kafkaretrydlq.exception.PermanentProcessingException;
import com.techlab.kafkaretrydlq.exception.TransientProcessingException;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FailureSimulationServiceTest {

    private final FailureSimulationService service = new FailureSimulationService();

    @Test
    void shouldPassWhenFailureModeIsNone() {
        OrderCreatedEvent event = new OrderCreatedEvent(
                "ORDER-1",
                "demo@example.com",
                BigDecimal.TEN,
                FailureMode.NONE,
                Instant.now()
        );

        Assertions.assertDoesNotThrow(() -> service.validate(event));
    }

    @Test
    void shouldThrowTransientOnceThenRecover() {
        OrderCreatedEvent event = new OrderCreatedEvent(
                "ORDER-2",
                "demo@example.com",
                BigDecimal.ONE,
                FailureMode.TRANSIENT_ONCE,
                Instant.now()
        );

        Assertions.assertThrows(TransientProcessingException.class, () -> service.validate(event));
        Assertions.assertDoesNotThrow(() -> service.validate(event));
    }

    @Test
    void shouldAlwaysThrowPermanentFailure() {
        OrderCreatedEvent event = new OrderCreatedEvent(
                "ORDER-3",
                "demo@example.com",
                BigDecimal.ONE,
                FailureMode.PERMANENT,
                Instant.now()
        );

        Assertions.assertThrows(PermanentProcessingException.class, () -> service.validate(event));
        Assertions.assertThrows(PermanentProcessingException.class, () -> service.validate(event));
    }
}
