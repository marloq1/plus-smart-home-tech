package ru.yandex.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment,String> {
}
