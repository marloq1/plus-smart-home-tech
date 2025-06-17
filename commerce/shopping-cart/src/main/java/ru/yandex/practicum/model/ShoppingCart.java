package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Table(name = "carts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_id", nullable = false)
    private String shoppingCartId;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "cart_state")
    private Boolean cartState;

    @ElementCollection
    @CollectionTable(
            name = "cart_products",
            joinColumns = @JoinColumn(name = "cart_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<String, Long> products;

}
