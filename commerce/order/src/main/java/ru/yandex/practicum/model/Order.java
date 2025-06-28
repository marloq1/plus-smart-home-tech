package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.dto.State;

import java.util.Map;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "cart_id", nullable = false)
    private String shoppingCartId;

    @ElementCollection
    @CollectionTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "cart_id", referencedColumnName = "cart_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<String, Long> products;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "user_name", nullable = false)
    private String userName;


    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "delivery_id")
    private String deliveryId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @Column(name = "delivery_weight", nullable = false)
    private Double deliveryWeight;

    @Column(name = "delivery_volume", nullable = false)
    private Double deliveryVolume;

    @Column(nullable = false)
    private Boolean fragile;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "delivery_price", nullable = false)
    private Double deliveryPrice;

    @Column(name = "product_price", nullable = false)
    private Double productPrice;

}
