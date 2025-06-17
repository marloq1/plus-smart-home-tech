package ru.yandex.practicum.dto;

import lombok.Data;

@Data
public class ChangeProductQuantityRequest {

    private String productId;
    private Long newQuantity;
}
