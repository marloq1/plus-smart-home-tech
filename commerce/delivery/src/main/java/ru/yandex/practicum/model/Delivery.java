package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id
    @Column(name = "delivery_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String deliveryId;

    @ManyToOne
    @JoinColumn(name = "from_address_id", nullable = false)
    private Address fromAddress;

    @ManyToOne
    @JoinColumn(name = "to_address_id", nullable = false)
    private Address toAddress;

    @Column(name = "order_id", nullable = false, length = 255)
    private String orderId;

    @Column(name = "delivery_state", nullable = false, length = 16)
    private String deliveryState;
}
