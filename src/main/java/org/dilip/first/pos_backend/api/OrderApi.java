package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.dao.OrderDao;
import org.dilip.first.pos_backend.entity.OrderEntity;
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

    public OrderEntity getById(Long orderId) {
        OrderEntity order = orderDao.findById(OrderEntity.class, orderId);
        if (order == null) {
            throw new ApiException( HttpStatus.BAD_REQUEST, "Order not found with id: " + orderId);
        }
        return order;
    }

    public List<OrderEntity> getAll(int page, int size) {
        return orderDao.getAll(page, size);
    }

    public List<OrderEntity> getByStatus(OrderStatus status, int page, int size) {
        return orderDao.findByStatus(status, page, size);
    }

    public List<OrderEntity> getByDateRange(OffsetDateTime from, OffsetDateTime to, int page, int size) {
        if (to.isBefore(from)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid date range");
        }
        return orderDao.findByDateRange(from, to, page, size);
    }

    public OrderEntity createOrder(Double totalAmount) {
        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(totalAmount);
        return orderDao.save(order);
    }
}

