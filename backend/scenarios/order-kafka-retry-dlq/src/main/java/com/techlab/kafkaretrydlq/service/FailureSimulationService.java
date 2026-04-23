package com.techlab.kafkaretrydlq.service;

import com.techlab.kafkaretrydlq.domain.FailureMode;
import com.techlab.kafkaretrydlq.domain.OrderCreatedEvent;
import com.techlab.kafkaretrydlq.exception.PermanentProcessingException;
import com.techlab.kafkaretrydlq.exception.TransientProcessingException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class FailureSimulationService {

    private final ConcurrentHashMap<String, AtomicInteger> attemptsByOrder = new ConcurrentHashMap<>();

    public void validate(OrderCreatedEvent event) {
        int currentAttempt = attemptsByOrder
                .computeIfAbsent(event.orderId(), ignored -> new AtomicInteger(0))
                .incrementAndGet();

        if (event.failureMode() == FailureMode.TRANSIENT_ONCE && currentAttempt == 1) {
            throw new TransientProcessingException("Temporary downstream timeout. Retry should recover.");
        }

        if (event.failureMode() == FailureMode.PERMANENT) {
            throw new PermanentProcessingException("Business validation failed. Message should go to DLQ.");
        }
    }
}
