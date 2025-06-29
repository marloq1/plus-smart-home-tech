package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "warehouse_product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WareHouseProduct {

    @Id
    @Column(name = "product_id")
    private String productId;

    private Boolean fragile;

    private Double width;

    private Double height;

    private Double depth;

    private Double weight;

    private Long quantity;
}
