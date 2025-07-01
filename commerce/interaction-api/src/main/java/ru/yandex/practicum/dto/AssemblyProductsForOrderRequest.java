package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AssemblyProductsForOrderRequest {

    private Map<String, Long> products;
    private String orderId;
}
