package ru.yandex.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,String> {

    Optional<Order> findByOrderId(String orderId);

    List<Order> findByUserName(String userId);
}
