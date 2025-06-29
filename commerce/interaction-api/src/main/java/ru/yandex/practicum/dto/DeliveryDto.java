package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryDto {
    private String deliveryId;
    private AddressDto fromAddress;
    private AddressDto toAddress;
    private String orderId;
    private DeliveryState deliveryState;
}
