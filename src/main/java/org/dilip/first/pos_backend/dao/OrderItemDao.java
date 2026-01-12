package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao<OrderItemEntity> {


    public List<OrderItemEntity> findByOrderId(Long orderId) {
        return em.createQuery(
                        "SELECT oi FROM OrderItemEntity oi WHERE oi.orderId = :orderId",
                        OrderItemEntity.class
                )
                .setParameter("orderId", orderId)
                .getResultList();
    }
}


