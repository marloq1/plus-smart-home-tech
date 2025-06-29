package ru.yandex.practicum.model;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.OrderDto;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    AddressDto toAddressDto(Address address);

    Address toAddress(AddressDto dto);

    OrderDto toOrderDto(Order order);

    Order toOrder(OrderDto orderDto);
}
