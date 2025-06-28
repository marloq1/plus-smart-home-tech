package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.dto.PaymentState;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.StoreClient;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.model.PaymentMapper;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final StoreClient storeClient;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;

    public PaymentDto createPayment(OrderDto orderDto) {
        PaymentDto paymentDto = PaymentDto.builder()
                .paymentId(UUID.randomUUID().toString())
                .totalPayment(orderDto.getTotalPrice())
                .deliveryTotal(orderDto.getDeliveryPrice())
                .feeTotal(0.1 * (orderDto.getTotalPrice() - orderDto.getDeliveryPrice()))
                .state(PaymentState.PENDING)
                .build();
        Payment payment = paymentMapper.toPayment(paymentDto);
        payment.setOrderId(orderDto.getOrderId());
        return paymentMapper.toPaymentDto(paymentRepository.save(payment));
    }

    public Double calculateCost(OrderDto orderDto) {
        Double totalCost = 0D;
        if (orderDto.getProducts() == null || orderDto.getProducts().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException("Недостаточно информации в заказе для расчёта");
        }
        for (Map.Entry<String, Long> entry : orderDto.getProducts().entrySet()) {
            totalCost += (storeClient.getProduct(entry.getKey()).getPrice() * entry.getValue());
        }
        return totalCost;
    }

    public Double calculateTotalCost(OrderDto orderDto) {
        if (orderDto.getProductPrice() != null && orderDto.getDeliveryPrice() != null) {
            return orderDto.getProductPrice() * 1.1 + orderDto.getDeliveryPrice();
        } else {
            throw new NotEnoughInfoInOrderToCalculateException("Недостаточно информации в заказе для расчёта");
        }
    }

    public void successfulPayment(String paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setState(PaymentState.SUCCESS);
        paymentRepository.save(payment);
        orderClient.successfulPayment(payment.getOrderId());
    }

    public void failedPayment(String paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setState(PaymentState.FAILED);
        paymentRepository.save(payment);
        orderClient.failedPayment(payment.getOrderId());
    }

    private Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new NoOrderFoundException("Платеж не найден"));
    }

}
