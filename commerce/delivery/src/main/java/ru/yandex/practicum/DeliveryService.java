package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.DeliveryState;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ShippedToDeliveryRequest;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.WareHouseClient;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.model.DeliveryMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final AddressRepository addressRepository;
    private final OrderClient orderClient;
    private final WareHouseClient wareHouseClient;

    @Transactional
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.toDelivery(deliveryDto);
        Address from = delivery.getFromAddress();
        Address to = delivery.getToAddress();
        from.setAddressId(from.getCountry());
        to.setAddressId(to.getCountry());
        if (!addressRepository.existsById(from.getAddressId())) {
            from = addressRepository.save(from);
        }
        if (!addressRepository.existsById(to.getAddressId())) {
            to = addressRepository.save(to);
        }
        delivery.setFromAddress(from);
        delivery.setToAddress(to);
        return deliveryMapper.toDeliveryDto(deliveryRepository.save(delivery));
    }

    @Transactional
    public void successfulDelivery(String orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка для этого заказа еще не сформирована"));
        delivery.setDeliveryState(DeliveryState.DELIVERED.name());
        deliveryRepository.save(delivery);
        orderClient.successfulDelivery(orderId);
    }

    @Transactional
    public void failedDelivery(String orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка для этого заказа еще не сформирована"));
        delivery.setDeliveryState(DeliveryState.FAILED.name());
        deliveryRepository.save(delivery);
        orderClient.failedDelivery(orderId);
    }

    @Transactional
    public void pickedDelivery(String orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка для этого заказа еще не сформирована"));
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS.name());
        deliveryRepository.save(delivery);
        orderClient.assemblyOrder(orderId);
        wareHouseClient.shippedToDelivery(ShippedToDeliveryRequest.builder()
                .orderId(orderId)
                .deliveryId(delivery.getDeliveryId()).build());
    }

    public Double calculateDelivery(OrderDto orderDto) {
        Double sum = 5D;
        Delivery delivery = deliveryRepository.findByOrderId(orderDto.getOrderId())
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка для этого заказа еще не сформирована"));
        log.info("Расчет суммарной стоимости доставки для заказ с id {}",orderDto.getOrderId());
        if (delivery.getFromAddress().getAddressId().equals("ADDRESS_1")) {
            sum += sum;
        } else if (delivery.getFromAddress().getAddressId().equals("ADDRESS_2")) {
            sum += sum * 2;
        }
        if (orderDto.getFragile() != null && orderDto.getFragile()) {
            sum += (0.2 * sum);
        }
        if (orderDto.getDeliveryWeight() != null) {
            sum += (0.3 * sum);
        }
        if (orderDto.getDeliveryVolume() != null) {
            sum += (0.2 * sum);
        }
        if (!delivery.getFromAddress().getAddressId().equals(delivery.getToAddress().getAddressId())) {
            sum += (0.2 * sum);
        }
        log.info("Суммарная стоимость доставки со склада {}, c показателем хрупкости {}, " +
                "с массой {}, с размерами {} по адресу {}", delivery.getToAddress().getAddressId(),
                orderDto.getFragile(),orderDto.getDeliveryWeight(),orderDto.getDeliveryVolume(),
                delivery.getToAddress().getAddressId());
        return sum;

    }
}
