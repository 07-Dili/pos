package org.dilip.first.pos_backend.dto;

import org.dilip.first.pos_backend.api.OrderApi;
import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.dilip.first.pos_backend.entity.OrderItemEntity;
import org.dilip.first.pos_backend.flow.OrderFlow;
import org.dilip.first.pos_backend.model.orders.OrderData;
import org.dilip.first.pos_backend.model.orders.OrderItemData;
import org.dilip.first.pos_backend.model.orders.OrderForm;
import org.dilip.first.pos_backend.model.orders.OrderItemForm;
import org.dilip.first.pos_backend.util.conversion.EntityToData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.dilip.first.pos_backend.util.conversion.EntityToData.convertOrderEntityToOrderData;

@Component
public class OrderDto {

    @Autowired
    private OrderFlow orderFlow;

    @Autowired
    private OrderApi orderApi;

    public OrderData create(OrderForm form) {

        List<OrderItemEntity> entities = form.getItems().stream().map(this::convertToEntity).toList();

        OrderEntity order = orderFlow.placeOrder(entities);

        return EntityToData.convertOrderEntityToOrderData(order);
    }

    public OrderData getById(Long id) {

        OrderEntity order = orderApi.getById(id);
        OrderData data = convertOrderEntityToOrderData(order);

        List<OrderItemEntity> items = orderFlow.getItemsByOrderId(id);

        data.setItems(
                items.stream()
                        .map(item -> {
                            OrderItemData d = new OrderItemData();
                            d.setBarcode(item.getBarcode());
                            d.setQuantity(item.getQuantity());
                            d.setSellingPrice(item.getSellingPrice());
                            return d;
                        })
                        .toList()
        );

        return data;
    }

    public List<OrderData> getByDateRange(LocalDate from, LocalDate to, int page, int size) {

        OffsetDateTime fromDT = from.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime toDT = to.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC);

        return orderApi.getByDateRange(fromDT, toDT, page, size)
                .stream()
                .map(EntityToData::convertOrderEntityToOrderData)
                .toList();
    }

    public List<OrderData> getByStatus(OrderStatus status, int page, int size) {
        return orderApi.getByStatus(status, page, size)
                .stream()
                .map(EntityToData::convertOrderEntityToOrderData)
                .toList();
    }

    public List<OrderData> getAll(int page, int size) {
        return orderApi.getAll(page, size)
                .stream()
                .map(EntityToData::convertOrderEntityToOrderData)
                .toList();
    }

    private OrderItemEntity convertToEntity(OrderItemForm form) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setBarcode(form.getBarcode());
        entity.setQuantity(form.getQuantity());
        entity.setSellingPrice(form.getSellingPrice());
        return entity;
    }
}
