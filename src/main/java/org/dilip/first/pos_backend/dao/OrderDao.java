package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao<OrderEntity> {

    public OrderDao() {
        super(OrderEntity.class);
    }

    static final String FIND_BY_STATUS_QUERY = """
        SELECT o FROM OrderEntity o WHERE o.status = :status
        ORDER BY o.createdAt DESC
        """;

    static final String FIND_BY_DATE_RANGE_QUERY = """
        SELECT o FROM OrderEntity o
        WHERE o.createdAt BETWEEN :from AND :to
        ORDER BY o.createdAt DESC
        """;

    public List<OrderEntity> getAll(int page, int size) {
        return findAll(page, size);
    }

    public List<OrderEntity> findByStatus(OrderStatus status, int page, int size) {
        return em.createQuery(FIND_BY_STATUS_QUERY, OrderEntity.class)
                .setParameter("status", status)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public List<OrderEntity> findByDateRange(OffsetDateTime from, OffsetDateTime to, int page, int size) {
        return em.createQuery(FIND_BY_DATE_RANGE_QUERY, OrderEntity.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }
}
