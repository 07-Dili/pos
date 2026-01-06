package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDao extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.barcode) = LOWER(:barcode)")
    ProductEntity findByBarcode(String barcode);

    @Query(
            value = """
        SELECT * FROM products p
        WHERE (:clientId IS NULL OR p.client_id = :clientId)
          AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:barcode IS NULL OR p.barcode = :barcode)
        ORDER BY p.id
        LIMIT :limit OFFSET :offset
        """,
            nativeQuery = true
    )
    List<ProductEntity> search(
            @Param("clientId") Long clientId,
            @Param("name") String name,
            @Param("barcode") String barcode,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query(
            value = """
        SELECT * FROM products
        ORDER BY id
        LIMIT :limit OFFSET :offset
        """,
            nativeQuery = true
    )
    List<ProductEntity> findAllWithPagination(
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
