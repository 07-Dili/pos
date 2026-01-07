package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.dao.OrderDao;
import org.dilip.first.pos_backend.dao.OrderItemDao;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.dilip.first.pos_backend.entity.OrderItemEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class OrderApi {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    public OrderEntity getById(Long id) {
        return orderDao.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Order not found "+id));
    }

    public List<OrderEntity> getAll(int page, int size) {
        int offset = page * size;
        return orderDao.findAll(size, offset);
    }

    public List<OrderEntity> getByStatus(OrderStatus status, int page, int size) {
        int offset = page * size;
        return orderDao.findByStatus(status, size, offset);
    }

    public List<OrderEntity> getByDateRange(
            OffsetDateTime from,
            OffsetDateTime to,
            int page,
            int size) {

        if (to.isBefore(from)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid date range");
        }

        int offset = page * size;
        return orderDao.findByDateRange(from, to, size, offset);
    }


    public List<OrderItemEntity> getItemsByOrderId(Long orderId) {
        return orderItemDao.findByOrderId(orderId);
    }


    public OrderEntity createOrder(Long userId) {
        OrderEntity order = new OrderEntity();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(0.0);
        return orderDao.save(order);
    }

    public void addItem(OrderItemEntity item) {
        orderItemDao.save(item);
    }

    public void updateTotal(OrderEntity order, Double total) {
        order.setTotalAmount(total);
        orderDao.save(order);
    }


}
