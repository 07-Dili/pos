package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientDao extends JpaRepository<ClientEntity, Long> {

    ClientEntity findByName(String name);

    @Query(
            value = """
        SELECT * FROM clients
        ORDER BY id
        LIMIT :limit OFFSET :offset
        """,
            nativeQuery = true
    )
    List<ClientEntity> findAllWithPagination(
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query(
            value = """
        SELECT * FROM clients c
        WHERE (:id IS NULL OR c.id = :id)
          AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')))
        ORDER BY c.id
        LIMIT :limit OFFSET :offset
        """,
            nativeQuery = true
    )
    List<ClientEntity> search(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("email") String email,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
