package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_delivery")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDelivery {

    @EmbeddedId
    private OrderDeliveryId id;

    public String getOrderId() {
        return id != null ? id.getOrderId() : null;
    }

    public String getDeliveryId() {
        return id != null ? id.getDeliveryId() : null;
    }
}
