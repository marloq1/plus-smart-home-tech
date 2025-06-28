package ru.yandex.practicum.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeliveryId implements Serializable {

    private String orderId;
    private String deliveryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDeliveryId that)) return false;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(deliveryId, that.deliveryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, deliveryId);
    }
}


