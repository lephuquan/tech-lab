package com.techlab.kafkaretrydlq.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProcessingRecordRepository extends JpaRepository<OrderProcessingRecord, Long> {
    Optional<OrderProcessingRecord> findByOrderId(String orderId);
}
