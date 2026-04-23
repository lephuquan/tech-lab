package com.techlab.kafkaretrydlq.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderCreatedEvent(
        String orderId,
        String customerEmail,
        BigDecimal totalAmount,
        FailureMode failureMode,
        Instant createdAt
) {
}
