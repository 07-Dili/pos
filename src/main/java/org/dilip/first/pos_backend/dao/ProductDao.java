package org.dilip.first.pos_backend.dao;

import jakarta.persistence.TypedQuery;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDao extends AbstractDao<ProductEntity> {

    public ProductDao() {
        super(ProductEntity.class);
    }

    static final String FIND_BY_BARCODE_QUERY = "SELECT p FROM ProductEntity p WHERE LOWER(p.barcode) = LOWER(:barcode)";

    static final String SEARCH_BASE_QUERY = "SELECT p FROM ProductEntity p WHERE 1=1";

    static final String SEARCH_ID_CONDITION = " AND p.id = :id";

    static final String SEARCH_CLIENT_ID_CONDITION = " AND p.clientId = :clientId";

    static final String SEARCH_NAME_CONDITION = " AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))";

    static final String SEARCH_BARCODE_CONDITION = " AND LOWER(p.barcode) LIKE LOWER(CONCAT('%', :barcode, '%'))";

    static final String SEARCH_ORDER_BY = " ORDER BY p.id";

    public ProductEntity findByBarcode(String barcode) {
        List<ProductEntity> result = em.createQuery( FIND_BY_BARCODE_QUERY, ProductEntity.class)
                .setParameter("barcode", barcode)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    public List<ProductEntity> search( Long id, Long clientId, String name, String barcode, int page, int size) {

        StringBuilder jpql = new StringBuilder(SEARCH_BASE_QUERY);

        if (id != null) {
            jpql.append(SEARCH_ID_CONDITION);
        }
        if (clientId != null) {
            jpql.append(SEARCH_CLIENT_ID_CONDITION);
        }
        if (name != null) {
            jpql.append(SEARCH_NAME_CONDITION);
        }
        if (barcode != null) {
            jpql.append(SEARCH_BARCODE_CONDITION);
        }

        jpql.append(SEARCH_ORDER_BY);

        TypedQuery<ProductEntity> query = em.createQuery(jpql.toString(), ProductEntity.class);

        if (id != null) query.setParameter("id", id);
        if (clientId != null) query.setParameter("clientId", clientId);
        if (name != null) query.setParameter("name", name);
        if (barcode != null) query.setParameter("barcode", barcode);

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }
}
