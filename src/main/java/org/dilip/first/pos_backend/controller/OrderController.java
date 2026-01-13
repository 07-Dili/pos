package org.dilip.first.pos_backend.controller;

import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.dto.OrderDto;
import org.dilip.first.pos_backend.model.orders.OrderData;
import org.dilip.first.pos_backend.model.orders.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderDto orderDto;

    @GetMapping
    public List<OrderData> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20")
    int size) {
        return orderDto.getAll(page, size);
    }

    @PostMapping
    public OrderData create(@Valid @RequestBody OrderForm form) {
        return orderDto.create(form);
    }

    @GetMapping("/status")
    public List<OrderData> getByStatus( @RequestParam OrderStatus status, @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size) {
        return orderDto.getByStatus(status, page, size);
    }

    @GetMapping("/{id}")
    public OrderData getById(@PathVariable Long id) {
        return orderDto.getById(id);
    }

    @GetMapping("/by-date")
    public List<OrderData> getByDateRange( @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate from,
                                           @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate to,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return orderDto.getByDateRange(from, to, page, size);
    }

}
