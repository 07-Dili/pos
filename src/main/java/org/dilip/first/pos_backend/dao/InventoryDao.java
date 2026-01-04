package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InventoryDao extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByProductId(Long productId);


    @Query("""
        SELECT i FROM InventoryEntity i
        JOIN i.product p
        WHERE (:barcode IS NULL OR LOWER(p.barcode) = LOWER(:barcode))
          AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
    Page<InventoryEntity> filter(String barcode, String name, Pageable pageable);

}
