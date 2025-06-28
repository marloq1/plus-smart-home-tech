package ru.yandex.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.OrderDelivery;
import ru.yandex.practicum.model.OrderDeliveryId;

public interface OrderDeliveryRepository extends JpaRepository<OrderDelivery, OrderDeliveryId> {
}
