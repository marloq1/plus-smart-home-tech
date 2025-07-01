package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @GetMapping
    public List<OrderDto> gerOrderByUserName(@RequestParam(name = "username") String userId) {
        return orderService.gerOrderByUserName(userId);
    }

    @PutMapping
    public OrderDto createOrder(@RequestBody CreateNewOrderRequest request) {
        return orderService.createOrder(request);
    }

    @PostMapping("/delivery")
    public void successfulDelivery(@RequestBody String orderId) {
        orderService.successfulDelivery(orderId);
    }

    @PostMapping("/return")
    public void returnOrder(@RequestBody ProductReturnRequest request) {
        orderService.returnOrder(request);
    }

    @PostMapping("/delivery/failed")
    public void failedDelivery(@RequestBody String orderId) {
        orderService.failedDelivery(orderId);
    }

    @PostMapping("/payment")
    public void successfulPayment(@RequestBody String orderId) {
        orderService.successfulPayment(orderId);
    }

    @PostMapping("/payment/failed")
    public void failedPayment(@RequestBody String orderId) {
        orderService.failedPayment(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotal(@RequestBody String orderId) {
        return orderService.calculateTotal(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDelivery(@RequestBody String orderId) {
        return orderService.calculateDelivery(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assemblyOrder(@RequestBody String orderId) {
        return orderService.assemblyOrder(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto assemblyFailed(@RequestBody String orderId) {
        return orderService.assemblyFailed(orderId);
    }

    @PostMapping("/completed")
    public OrderDto completeOrder(@RequestBody String orderId) {
        return orderService.completeOrder(orderId);
    }
}
