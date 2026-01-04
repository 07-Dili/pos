package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.constants.ApiStatus;
import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.dao.OrderDao;
import org.dilip.first.pos_backend.dao.OrderItemDao;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.dilip.first.pos_backend.entity.OrderItemEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;

@Service
@Transactional
public class OrderApi {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    public OrderEntity getById(Long id) {
        return orderDao.findById(id).orElseThrow(() -> new ApiException(404, "Order not found "+id));
    }

    public Page<OrderEntity> getByStatus(OrderStatus status, Pageable pageable) {
        return orderDao.findByStatus(status, pageable);
    }

    public Page<OrderEntity> getByDateRange(OffsetDateTime from, OffsetDateTime to, Pageable pageable) {

        if (from == null || to == null) {
            throw new ApiException(400, "From date and To date are required");
        }

        if (to.isBefore(from)) {
            throw new ApiException(400, "'to' date must be equal to or after 'from' date "+from+" "+to);
        }

        return orderDao.findByDateRange(from, to, pageable);

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
