package org.dilip.first.pos_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.dilip.first.pos_backend.constants.OrderStatus;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private Double totalAmount;
}
