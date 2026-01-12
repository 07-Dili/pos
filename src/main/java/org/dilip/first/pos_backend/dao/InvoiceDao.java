package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.entity.InvoiceEntity;
import org.springframework.stereotype.Repository;

@Repository
public class InvoiceDao extends AbstractDao<InvoiceEntity> {

    static final String FIND_BY_ORDER_ID_QUERY = "SELECT i FROM InvoiceEntity i WHERE i.orderId = :orderId";

    public InvoiceDao() {
        super(InvoiceEntity.class);
    }

    public InvoiceEntity findByOrderId(Long orderId) {
        return em.createQuery(FIND_BY_ORDER_ID_QUERY, InvoiceEntity.class)
                .setParameter("orderId", orderId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
