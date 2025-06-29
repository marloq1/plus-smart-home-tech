package ru.yandex.practicum.model;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.PaymentDto;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDto toPaymentDto(Payment payment);

    Payment toPayment(PaymentDto dto);
}
