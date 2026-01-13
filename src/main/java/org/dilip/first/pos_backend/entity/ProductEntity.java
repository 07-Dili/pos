package org.dilip.first.pos_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "products",
        uniqueConstraints = { @UniqueConstraint(columnNames = "barcode")},
        indexes = {
                @Index(name = "idx_product_barcode", columnList = "barcode")
        }
)
public class ProductEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String barcode;

    @Column(nullable = false)
    private Double mrp;

}
