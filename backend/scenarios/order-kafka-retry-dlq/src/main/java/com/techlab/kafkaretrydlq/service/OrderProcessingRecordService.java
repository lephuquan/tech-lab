package com.techlab.kafkaretrydlq.service;

import com.techlab.kafkaretrydlq.domain.OrderCreatedEvent;
import com.techlab.kafkaretrydlq.domain.ProcessingStatus;
import com.techlab.kafkaretrydlq.persistence.OrderProcessingRecord;
import com.techlab.kafkaretrydlq.persistence.OrderProcessingRecordRepository;
import com.techlab.kafkaretrydlq.web.OrderProcessingRecordResponse;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderProcessingRecordService {

    private final OrderProcessingRecordRepository repository;

    public OrderProcessingRecordService(OrderProcessingRecordRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void markReceived(OrderCreatedEvent event, String topic, int attempt) {
        OrderProcessingRecord record = repository.findByOrderId(event.orderId())
                .orElseGet(OrderProcessingRecord::new);
        record.setOrderId(event.orderId());
        record.setCustomerEmail(event.customerEmail());
        record.setAttempts(Math.max(record.getAttempts(), attempt));
        record.setStatus(ProcessingStatus.RECEIVED);
        record.setLastTopic(topic);
        record.setUpdatedAt(Instant.now());
        repository.save(record);
    }

    @Transactional
    public void markRetrying(String orderId, String topic, int attempt, String errorMessage) {
        OrderProcessingRecord record = repository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("Missing record for order: " + orderId));
        record.setAttempts(Math.max(record.getAttempts(), attempt));
        record.setStatus(ProcessingStatus.RETRYING);
        record.setLastTopic(topic);
        record.setLastError(errorMessage);
        record.setUpdatedAt(Instant.now());
        repository.save(record);
    }

    @Transactional
    public void markProcessed(String orderId, String topic, int attempt) {
        OrderProcessingRecord record = repository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("Missing record for order: " + orderId));
        record.setAttempts(Math.max(record.getAttempts(), attempt));
        record.setStatus(ProcessingStatus.PROCESSED);
        record.setLastTopic(topic);
        record.setLastError(null);
        record.setUpdatedAt(Instant.now());
        repository.save(record);
    }

    @Transactional
    public void markDlq(OrderCreatedEvent event, String dltTopic, String errorMessage) {
        OrderProcessingRecord record = repository.findByOrderId(event.orderId())
                .orElseGet(OrderProcessingRecord::new);
        record.setOrderId(event.orderId());
        record.setCustomerEmail(event.customerEmail());
        record.setAttempts(Math.max(record.getAttempts(), 1));
        record.setStatus(ProcessingStatus.DLQ);
        record.setLastTopic(dltTopic);
        record.setLastError(errorMessage);
        record.setUpdatedAt(Instant.now());
        repository.save(record);
    }

    @Transactional(readOnly = true)
    public List<OrderProcessingRecordResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderProcessingRecordResponse findByOrderId(String orderId) {
        return repository.findByOrderId(orderId)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    private OrderProcessingRecordResponse toResponse(OrderProcessingRecord record) {
        return new OrderProcessingRecordResponse(
                record.getOrderId(),
                record.getCustomerEmail(),
                record.getStatus(),
                record.getAttempts(),
                record.getLastTopic(),
                record.getLastError(),
                record.getUpdatedAt()
        );
    }
}
