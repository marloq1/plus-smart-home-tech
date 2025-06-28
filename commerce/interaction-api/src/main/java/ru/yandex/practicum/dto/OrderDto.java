package ru.yandex.practicum.dto;


import lombok.Data;

import java.util.Map;

@Data
public class OrderDto {

    private String orderId;
    private String shoppingCartId;
    private Map<String, Long> products;
    private String paymentId;
    private String deliveryId;
    private State state;
    private Double deliveryWeight;
    private Double deliveryVolume;
    private Boolean fragile;
    private Double totalPrice;
    private Double deliveryPrice;
    private Double productPrice;


}
