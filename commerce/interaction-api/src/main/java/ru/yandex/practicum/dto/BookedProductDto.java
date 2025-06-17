package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookedProductDto {

    private Double deliveryWeight;
    private Double deliveryVolume;
    private Boolean fragile;

}
