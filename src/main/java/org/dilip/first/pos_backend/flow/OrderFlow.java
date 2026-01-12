package org.dilip.first.pos_backend.flow;

import org.dilip.first.pos_backend.api.InventoryApi;
import org.dilip.first.pos_backend.api.OrderApi;
import org.dilip.first.pos_backend.api.ProductApi;
import org.dilip.first.pos_backend.dao.OrderItemDao;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.dilip.first.pos_backend.entity.OrderItemEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.model.orders.OrderItemForm;
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
    private InventoryApi inventoryApi;

    @Autowired
    private OrderItemDao orderItemDao;

    public OrderEntity placeOrder(List<OrderItemForm> items) {

        OrderEntity order = orderApi.createOrder();
        double total = 0;

        for (OrderItemForm form : items) {

            ProductEntity product = productApi.getByBarcode(form.getBarcode());

            if (form.getSellingPrice() < product.getMrp()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Selling price below MRP " + form.getSellingPrice() + " < " + product.getMrp());
            }

            inventoryApi.reduce(product.getId(), form.getQuantity());

            OrderItemEntity item = new OrderItemEntity();
            item.setOrderId(order.getId());
            item.setProductId(product.getId());
            item.setBarcode(product.getBarcode());
            item.setQuantity(form.getQuantity());
            item.setSellingPrice(form.getSellingPrice());

            orderItemDao.save(item);

            total += form.getQuantity() * form.getSellingPrice();
        }

        orderApi.updateTotal(order, total);
        return order;
    }
    public List<OrderItemEntity> getItemsByOrderId(Long orderId) {
        return orderItemDao.findByOrderId(orderId);
    }
}
