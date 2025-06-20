package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetProductQuantityStateRequest {

    @NotBlank
    private String productId;
    @NotNull
    private QuantityState quantityState;
}
