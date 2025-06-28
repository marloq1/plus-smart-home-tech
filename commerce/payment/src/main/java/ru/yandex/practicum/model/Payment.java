package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.dto.PaymentState;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "total_payment")
    private Double totalPayment;

    @Column(name = "delivery_total")
    private Double deliveryTotal;

    @Column(name = "fee_total")
    private Double feeTotal;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_state")
    private PaymentState state;
}
