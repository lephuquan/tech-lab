package com.techlab.kafkaretrydlq.web;

import com.techlab.kafkaretrydlq.domain.ProcessingStatus;
import java.time.Instant;

public record OrderProcessingRecordResponse(
        String orderId,
        String customerEmail,
        ProcessingStatus status,
        int attempts,
        String lastTopic,
        String lastError,
        Instant updatedAt
) {
}
