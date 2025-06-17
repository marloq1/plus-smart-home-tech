package ru.yandex.practicum.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {

    public PageResponse(List<T> content, List<SortField> sort) {
        this.content = content;
        this.sort = sort;
    }

    private List<T> content;
    private List<SortField> sort;
}
