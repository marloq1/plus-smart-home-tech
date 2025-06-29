package ru.yandex.practicum.dto;

import lombok.Data;

@Data
public class CreateNewOrderRequest {

    private ShoppingCartDto shoppingCart;
    private AddressDto deliveryAddress;
}
