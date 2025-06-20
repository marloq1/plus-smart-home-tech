package ru.yandex.practicum.dto;

import lombok.Data;

@Data
public class AddProductToWareHouseRequest {

    private String productId;
    private Integer Quantity;
}
