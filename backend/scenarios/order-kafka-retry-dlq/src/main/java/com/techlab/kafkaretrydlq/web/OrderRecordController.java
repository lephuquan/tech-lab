package com.techlab.kafkaretrydlq.web;

import com.techlab.kafkaretrydlq.service.OrderProcessingRecordService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders/records")
public class OrderRecordController {

    private final OrderProcessingRecordService orderProcessingRecordService;

    public OrderRecordController(OrderProcessingRecordService orderProcessingRecordService) {
        this.orderProcessingRecordService = orderProcessingRecordService;
    }

    @GetMapping
    public List<OrderProcessingRecordResponse> findAll() {
        return orderProcessingRecordService.findAll();
    }

    @GetMapping("/{orderId}")
    public OrderProcessingRecordResponse findByOrderId(@PathVariable String orderId) {
        return orderProcessingRecordService.findByOrderId(orderId);
    }
}
