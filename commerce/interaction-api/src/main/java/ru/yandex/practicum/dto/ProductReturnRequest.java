package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ProductReturnRequest {

    private String orderId;
    private Map<String, Long> products;
}
