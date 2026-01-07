package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface OrderDao extends JpaRepository<OrderEntity, Long> {

    @Query(
            value = """
    SELECT * FROM orders
    ORDER BY created_at DESC
    LIMIT :limit OFFSET :offset
    """,
            nativeQuery = true
    )
    List<OrderEntity> findAll(
            @Param("limit") int limit,
            @Param("offset") int offset
    );


    @Query(
            value = """
    SELECT * FROM orders
    WHERE created_at BETWEEN :from AND :to
    ORDER BY created_at DESC
    LIMIT :limit OFFSET :offset
    """,
            nativeQuery = true
    )
    List<OrderEntity> findByDateRange(
            @Param("from") OffsetDateTime from,
            @Param("to") OffsetDateTime to,
            @Param("limit") int limit,
            @Param("offset") int offset
    );


    @Query(
            value = """
    SELECT * FROM orders
    WHERE status = :status
    ORDER BY created_at DESC
    LIMIT :limit OFFSET :offset
    """,
            nativeQuery = true
    )
    List<OrderEntity> findByStatus(
            @Param("status") String status,
            @Param("limit") int limit,
            @Param("offset") int offset
    );




}
