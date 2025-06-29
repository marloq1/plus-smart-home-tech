package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

@RestController
@RequestMapping(path = "/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto createPayment(@RequestBody OrderDto orderDto) {
        return paymentService.createPayment(orderDto);
    }

    @PostMapping("/productCost")
    public Double calculateCost(@RequestBody OrderDto orderDto) {
        return paymentService.calculateCost(orderDto);
    }

    @PostMapping("/totalCost")
    public Double calculateTotalCost(@RequestBody OrderDto orderDto) {
        return paymentService.calculateTotalCost(orderDto);
    }

    @PostMapping("/refund")
    public void successfulPayment(@RequestBody String paymentId) {
        paymentService.successfulPayment(paymentId);
    }

    @PostMapping("/failed")
    public void failedPayment(@RequestBody String paymentId) {
        paymentService.failedPayment(paymentId);
    }


}
