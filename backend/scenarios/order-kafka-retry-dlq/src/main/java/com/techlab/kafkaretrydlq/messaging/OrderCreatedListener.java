package com.techlab.kafkaretrydlq.messaging;

import com.techlab.kafkaretrydlq.domain.OrderCreatedEvent;
import com.techlab.kafkaretrydlq.exception.TransientProcessingException;
import com.techlab.kafkaretrydlq.service.OrderProcessingRecordService;
import com.techlab.kafkaretrydlq.service.OrderProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final OrderProcessingService orderProcessingService;
    private final OrderProcessingRecordService recordService;

    public OrderCreatedListener(
            OrderProcessingService orderProcessingService,
            OrderProcessingRecordService recordService
    ) {
        this.orderProcessingService = orderProcessingService;
        this.recordService = recordService;
    }

    @RetryableTopic(
            attempts = "${app.kafka.retry.attempts}",
            backoff = @Backoff(
                    delayExpression = "${app.kafka.retry.delay-ms}",
                    multiplierExpression = "${app.kafka.retry.multiplier}"
            ),
            include = {TransientProcessingException.class},
            autoCreateTopics = "false",
            retryTopicSuffix = ".retry",
            dltTopicSuffix = ".dlt"
    )
    @KafkaListener(
            topics = "${app.kafka.topics.order-created}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onMessage(
            OrderCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(name = KafkaHeaders.DELIVERY_ATTEMPT, required = false) Integer deliveryAttempt
    ) {
        int attempt = deliveryAttempt == null ? 1 : deliveryAttempt;
        recordService.markReceived(event, topic, attempt);

        try {
            orderProcessingService.process(event);
            recordService.markProcessed(event.orderId(), topic, attempt);
        } catch (RuntimeException ex) {
            recordService.markRetrying(event.orderId(), topic, attempt, ex.getMessage());
            log.warn("Order {} failed at topic {} attempt {}: {}", event.orderId(), topic, attempt, ex.getMessage());
            throw ex;
        }
    }

    @DltHandler
    public void onDeadLetter(
            OrderCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(name = KafkaHeaders.DLT_EXCEPTION_MESSAGE, required = false) String exceptionMessage
    ) {
        String error = exceptionMessage == null ? "Unknown error" : exceptionMessage;
        log.error("Order {} moved to DLQ topic {} with error: {}", event.orderId(), topic, error);
        recordService.markDlq(event, topic, error);
    }
}
