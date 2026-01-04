package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface OrderDao extends JpaRepository<OrderEntity, Long> {

    @Query("""
    SELECT o FROM OrderEntity o
    WHERE (:from IS NULL OR o.createdAt >= :from)
      AND (:to IS NULL OR o.createdAt <= :to)
""")
    Page<OrderEntity> findByDateRange( @Param("from") OffsetDateTime from, @Param("to") OffsetDateTime to, Pageable pageable);

    Page<OrderEntity> findByStatus( OrderStatus status, Pageable pageable);

}
