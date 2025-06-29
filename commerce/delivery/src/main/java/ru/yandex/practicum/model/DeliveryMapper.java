package ru.yandex.practicum.model;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.DeliveryDto;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    AddressDto toAddressDto(Address address);

    Address toAddress(AddressDto dto);

    DeliveryDto toDeliveryDto(Delivery delivery);

    Delivery toDelivery(DeliveryDto dto);

}
