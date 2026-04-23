package com.techlab.kafkaretrydlq.service;

import com.techlab.kafkaretrydlq.domain.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingService {

    private static final Logger log = LoggerFactory.getLogger(OrderProcessingService.class);

    private final FailureSimulationService failureSimulationService;

    public OrderProcessingService(FailureSimulationService failureSimulationService) {
        this.failureSimulationService = failureSimulationService;
    }

    public void process(OrderCreatedEvent event) {
        failureSimulationService.validate(event);
        log.info("Order {} processed successfully for {}", event.orderId(), event.customerEmail());
    }
}
