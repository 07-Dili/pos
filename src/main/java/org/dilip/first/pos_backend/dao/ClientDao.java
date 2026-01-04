package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.entity.ClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientDao extends JpaRepository<ClientEntity, Long> {

    ClientEntity findByName(String name);

    @Query("""
        SELECT c FROM ClientEntity c
        WHERE (:id IS NULL OR c.id = :id)
          AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
    List<ClientEntity> filter(Long id, String name);

    @Query("SELECT c FROM ClientEntity c WHERE c.name LIKE %:name%")
    Page<ClientEntity> searchByName(String name, Pageable pageable);
}
