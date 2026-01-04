package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductDao extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.barcode) = LOWER(:barcode)")
    ProductEntity findByBarcode(String barcode);

    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    @Query("""
        SELECT p FROM ProductEntity p
        WHERE (:clientId IS NULL OR p.clientId = :clientId)
          AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
    Page<ProductEntity> filter(Long clientId, String name, Pageable pageable);

}

