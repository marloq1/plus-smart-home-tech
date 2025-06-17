package ru.yandex.practicum.model;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;

@Mapper(componentModel = "spring")
public interface WareHouseMapper {

    @Mapping(source = "dimension.width", target = "width")
    @Mapping(source = "dimension.height", target = "height")
    @Mapping(source = "dimension.depth", target = "depth")
    WareHouseProduct toEntity(NewProductInWarehouseRequest request);


    @Mapping(source = "width", target = "dimension.width")
    @Mapping(source = "height", target = "dimension.height")
    @Mapping(source = "depth", target = "dimension.depth")
    NewProductInWarehouseRequest toDto(WareHouseProduct wareHouse);
}
