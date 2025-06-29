package ru.yandex.practicum.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeliveryId implements Serializable {

    private String orderId;
    private String deliveryId;


}


