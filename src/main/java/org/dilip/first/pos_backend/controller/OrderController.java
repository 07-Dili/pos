package org.dilip.first.pos_backend.controller;

import jakarta.validation.Valid;
import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.dto.OrderDto;
import org.dilip.first.pos_backend.model.data.OrderData;
import org.dilip.first.pos_backend.model.form.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderDto orderDto;

    @PostMapping
    public OrderData create(@RequestHeader("userId") Long userId, @Valid @RequestBody OrderForm form) {
        return orderDto.create(userId, form);
    }

    @GetMapping("/status")
    public Page<OrderData> getByStatus(@RequestParam OrderStatus status, Pageable pageable) {
        return orderDto.getByStatus(status, pageable);
    }

    @GetMapping("/{id}")
    public OrderData getById(@PathVariable Long id) {
        return orderDto.getById(id);
    }

    @GetMapping("/orders/by-date")
    public Page<OrderData> getByDateRange(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate from,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate to, Pageable pageable) {
        return orderDto.getByDateRange(from, to, pageable);
    }

}
