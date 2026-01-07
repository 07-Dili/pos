package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.model.data.FilterResponseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryDao extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByProductId(Long productId);

    @Query(
            value = """
    SELECT
        p.id AS productId,
        p.client_id AS clientId,
        p.name AS productName,
        p.barcode AS barcode,
        p.mrp AS mrp,
        i.quantity AS quantity
    FROM inventory i
    JOIN products p ON i.product_id = p.id
    WHERE (:productId IS NULL OR p.id = :productId)
      AND (:barcode IS NULL OR LOWER(p.barcode) LIKE LOWER(CONCAT('%', :barcode, '%')))
      AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
    ORDER BY p.name
    LIMIT :limit OFFSET :offset
    """,
            nativeQuery = true
    )
    List<FilterResponseData> filter(
            @Param("productId") Long productId,
            @Param("barcode") String barcode,
            @Param("name") String name,
            @Param("limit") int limit,
            @Param("offset") int offset
    );




    @Query(
            value = """
        SELECT * FROM inventory
        ORDER BY product_id
        LIMIT :limit OFFSET :offset
        """,
            nativeQuery = true
    )
    List<InventoryEntity> findAll(
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
