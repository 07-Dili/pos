package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.model.inventory.InventoryFilterResponseData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InventoryDao extends AbstractDao<InventoryEntity> {


    static final String FIND_BY_PRODUCT_ID_QUERY = "SELECT i FROM InventoryEntity i WHERE i.product.id = :productId";
    static final String GET_ALL_INVENTORY_QUERY = "SELECT i FROM InventoryEntity i where i.product.id in (:productIds)";
    static final String FILTER_QUERY = """
        SELECT new InventoryFilterResponseData(p.id,p.clientId,p.name,p.barcode,p.mrp,i.quantity)
        FROM InventoryEntity i
        JOIN i.product p
        WHERE (:productId IS NULL OR p.id = :productId)
          AND (:barcode IS NULL OR LOWER(p.barcode) LIKE LOWER(CONCAT('%', :barcode, '%')))
          AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
        ORDER BY p.name
        """;

    public InventoryEntity findByProductId(Long productId) {
        return em.createQuery(FIND_BY_PRODUCT_ID_QUERY, InventoryEntity.class)
                .setParameter("productId", productId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<InventoryFilterResponseData> filter(Long productId, String barcode, String name, int page, int size) {
        return em.createQuery(FILTER_QUERY, InventoryFilterResponseData.class)
                .setParameter("productId", productId)
                .setParameter("barcode", barcode)
                .setParameter("name", name)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public List<InventoryEntity> getAllWithoutPagination(List<Long> productIds)
    {
        return em.createQuery(GET_ALL_INVENTORY_QUERY, InventoryEntity.class)
                .setParameter("productIds", productIds)
                .getResultList();
    }
}
