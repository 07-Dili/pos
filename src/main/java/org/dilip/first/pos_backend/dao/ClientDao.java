package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.entity.ClientEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientDao extends AbstractDao<ClientEntity> {

    static final String FIND_BY_NAME_QUERY = "SELECT c FROM ClientEntity c WHERE LOWER(c.name) = LOWER(:name)";

    static final String FIND_BY_EMAIL_QUERY = " SELECT c FROM ClientEntity c WHERE LOWER(c.email) = LOWER(:email)";

    static final String SEARCH_QUERY = """
        SELECT c FROM ClientEntity c
        WHERE (:id IS NULL OR c.id = :id)
          AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')))
        ORDER BY c.id
        """;

    public ClientEntity findByName(String name) {
        List<ClientEntity> result = em.createQuery( FIND_BY_NAME_QUERY, ClientEntity.class)
                .setParameter("name", name).getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    public ClientEntity findByEmail(String email) {
        List<ClientEntity> result = em.createQuery( FIND_BY_EMAIL_QUERY, ClientEntity.class)
                .setParameter("email", email).getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    public List<ClientEntity> search( Long id, String name, String email, int page, int size) {
        return em.createQuery( SEARCH_QUERY, ClientEntity.class)
                .setParameter("id", id)
                .setParameter("name", name)
                .setParameter("email", email)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }
}
