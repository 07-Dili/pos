package org.dilip.first.pos_backend.flow;

import org.dilip.first.pos_backend.api.OrderApi;
import org.dilip.first.pos_backend.api.ProductApi;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.dilip.first.pos_backend.entity.OrderItemEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class OrderFlow {

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private InventoryFlow inventoryFlow;

    public OrderEntity placeOrder(List<OrderItemEntity> items) {

        double total = 0;

        for (OrderItemEntity item : items) {

            ProductEntity product = productApi.getByBarcode(item.getBarcode());

            if (item.getSellingPrice() < product.getMrp()) {
                throw new ApiException( HttpStatus.BAD_REQUEST, "Selling price below MRP for barcode " + product.getBarcode());
            }

            inventoryFlow.validateAvailability(product.getBarcode(), item.getQuantity());

            total += item.getQuantity() * item.getSellingPrice();
        }

        OrderEntity order = orderApi.createOrder(total);
        for (OrderItemEntity item : items) {

            ProductEntity product = productApi.getByBarcode(item.getBarcode());

            inventoryFlow.reduceByBarcode(product.getBarcode(), item.getQuantity());

            orderApi.saveOrderItem(item,order.getId(),product.getId());

        }

        return order;
    }

    public List<OrderItemEntity> getItemsByOrderId(Long orderId) {
        return orderApi.findOrderItemsByOrderId(orderId);
    }
}
