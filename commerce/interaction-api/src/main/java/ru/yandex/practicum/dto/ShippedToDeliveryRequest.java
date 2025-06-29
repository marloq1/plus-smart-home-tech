package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShippedToDeliveryRequest {

    private String orderId;
    private String deliveryId;
}
