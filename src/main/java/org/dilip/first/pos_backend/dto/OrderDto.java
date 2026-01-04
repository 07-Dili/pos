package org.dilip.first.pos_backend.dto;

import org.dilip.first.pos_backend.api.OrderApi;
import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.dilip.first.pos_backend.flow.OrderFlow;
import org.dilip.first.pos_backend.model.data.OrderData;
import org.dilip.first.pos_backend.model.form.OrderForm;
import org.dilip.first.pos_backend.util.conversion.EntityToData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.dilip.first.pos_backend.util.conversion.EntityToData.convertOrderEntityToOrderData;

@Component
public class OrderDto {

    @Autowired
    private OrderFlow orderFlow;

    @Autowired
    private OrderApi orderApi;

    public OrderData create(Long userId, OrderForm form) {
        OrderEntity order = orderFlow.placeOrder(userId, form.getItems());
        return convertOrderEntityToOrderData(order);
    }

    public OrderData getById(Long id) {
        return convertOrderEntityToOrderData(orderApi.getById(id));
    }



    public Page<OrderData> getByDateRange(LocalDate from, LocalDate to, Pageable pageable) {
        OffsetDateTime fromDateTime = from.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime toDateTime = to.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC);
        return orderApi.getByDateRange(fromDateTime, toDateTime, pageable).map(EntityToData::convertOrderEntityToOrderData);
    }

    public Page<OrderData> getByStatus(OrderStatus status,Pageable pageable) {
        return orderApi.getByStatus(status, pageable).map(EntityToData::convertOrderEntityToOrderData);
    }

}
