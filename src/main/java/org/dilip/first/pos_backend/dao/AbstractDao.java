package org.dilip.first.pos_backend.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

public abstract class AbstractDao<T> {

    @PersistenceContext
    protected EntityManager em;

    private final Class<T> entityClass;

    protected AbstractDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T findById(Long id) {
        return em.find(entityClass, id);
    }

    public List<T> findAll(int page, int size) {
        return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public T save(T entity) {
        return em.merge(entity);
    }

}
