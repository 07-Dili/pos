package org.dilip.first.pos_backend.model.data;

import lombok.Getter;
import lombok.Setter;
import org.dilip.first.pos_backend.constants.OrderStatus;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class OrderData {

    private Long id;
    private OrderStatus status;
    private Double totalAmount;
    private OffsetDateTime createdAt;
    private List<OrderItemData> items;
}
