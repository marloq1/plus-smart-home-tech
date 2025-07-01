package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient {

    @PostMapping("/delivery")
    void successfulDelivery(@RequestBody String orderId);

    @PostMapping("/assembly")
    OrderDto assemblyOrder(@RequestBody String orderId);

    @PostMapping("/delivery/failed")
    void failedDelivery(@RequestBody String orderId);

    @PostMapping("/payment")
    void successfulPayment(@RequestBody String orderId);

    @PostMapping("/payment/failed")
    void failedPayment(@RequestBody String orderId);

    @PostMapping("/assembly/failed")
    OrderDto assemblyFailed(@RequestBody String orderId);


}
