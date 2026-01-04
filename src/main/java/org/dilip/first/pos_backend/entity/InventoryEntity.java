package org.dilip.first.pos_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "inventory",
        uniqueConstraints = { @UniqueConstraint( name = "uk_inventory_product", columnNames = "product_id") },
        indexes = { @Index(name = "idx_inventory_product", columnList = "product_id")}
)
public class InventoryEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private Long quantity;
}
