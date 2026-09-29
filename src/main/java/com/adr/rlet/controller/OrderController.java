package com.adr.rlet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adr.rlet.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public String place(@RequestParam String item, @RequestParam int quantity) {
        return orderService.placeOrder(item, quantity);
    }

    @GetMapping("/total")
    public double total(@RequestParam double price, @RequestParam int quantity) {
        return orderService.calculateTotal(price, quantity);
    }
    
    @PostMapping("/validated")
    public String placeValidated(@RequestParam String item, @RequestParam int quantity) {
        return orderService.placeOrderWithValidation(item, quantity);
    }
    
    @PostMapping("/audited")
    public String placeAudited(@RequestParam String item, @RequestParam int quantity) {
        return orderService.placeAuditedOrder(item, quantity);
    }
}