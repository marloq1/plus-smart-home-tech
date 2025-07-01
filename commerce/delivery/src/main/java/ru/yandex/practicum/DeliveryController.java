package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

@RestController
@RequestMapping(path = "/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto createDelivery(@RequestBody DeliveryDto deliveryDto) {
        return deliveryService.createDelivery(deliveryDto);
    }

    @PostMapping("/successful")
    public void successfulDelivery(@RequestBody String orderId) {
        deliveryService.successfulDelivery(orderId);
    }

    @PostMapping("/failed")
    public void failedDelivery(@RequestBody String orderId) {
        deliveryService.failedDelivery(orderId);
    }

    @PostMapping("/picked")
    public void pickedDelivery(@RequestBody String orderId) {
        deliveryService.pickedDelivery(orderId);
    }

    @PostMapping("/cost")
    public Double calculateDelivery(@RequestBody OrderDto orderDto) {
        return deliveryService.calculateDelivery(orderDto);
    }
}
