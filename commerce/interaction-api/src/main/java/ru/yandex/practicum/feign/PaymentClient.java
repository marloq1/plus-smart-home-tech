package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping
    PaymentDto createPayment(@RequestBody OrderDto orderDto);

    @PostMapping("/productCost")
    Double calculateCost(@RequestBody OrderDto orderDto);

    @PostMapping("/totalCost")
    Double calculateTotalCost(@RequestBody OrderDto orderDto);


}
