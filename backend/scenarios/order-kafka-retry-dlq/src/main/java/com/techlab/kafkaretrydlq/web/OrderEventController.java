package com.techlab.kafkaretrydlq.web;

import com.techlab.kafkaretrydlq.domain.OrderCreatedEvent;
import com.techlab.kafkaretrydlq.service.OrderEventProducer;
import jakarta.validation.Valid;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderEventController {

    private final OrderEventProducer orderEventProducer;

    public OrderEventController(OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createOrderEvent(@Valid @RequestBody CreateOrderEventRequest request) {
        OrderCreatedEvent event = new OrderCreatedEvent(
                request.orderId(),
                request.customerEmail(),
                request.totalAmount(),
                request.failureMode(),
                Instant.now()
        );
        orderEventProducer.publish(event);
    }
}
