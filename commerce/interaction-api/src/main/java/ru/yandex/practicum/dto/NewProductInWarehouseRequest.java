package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewProductInWarehouseRequest {

    @NotNull
    private String productId;
    @NotNull
    private Boolean fragile;
    @NotNull
    private DimensionDto dimension;
    @NotNull
    private Double weight;
}
