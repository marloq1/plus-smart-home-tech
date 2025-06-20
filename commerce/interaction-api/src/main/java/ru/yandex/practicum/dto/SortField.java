package ru.yandex.practicum.dto;

import lombok.Data;

@Data
public class SortField {

    private String property;
    private String direction;

    public SortField(String property, String direction) {
        this.property = property;
        this.direction = direction;
    }
}
