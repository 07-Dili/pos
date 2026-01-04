package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceDao extends JpaRepository<InvoiceEntity, Long> {

    Optional<InvoiceEntity> findByOrderId(Long orderId);
}
