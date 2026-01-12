package org.dilip.first.pos_backend.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

public abstract class AbstractDao<T> {

    @PersistenceContext
    protected EntityManager em;

    public T findById(Class<T> cla,Long id) {
        return em.find(cla, id);
    }

    public List<T> findAll(Class<T>cla,int page, int size) {
        return em.createQuery("SELECT e FROM " + cla.getSimpleName() + " e", cla)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public T save(T entity) {
        return em.merge(entity);
    }

}
