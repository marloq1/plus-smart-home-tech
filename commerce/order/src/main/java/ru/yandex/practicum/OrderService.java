package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.feign.DeliveryClient;
import ru.yandex.practicum.feign.PaymentClient;
import ru.yandex.practicum.feign.WareHouseClient;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.AddressRepository;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderMapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WareHouseClient wareHouseClient;
    private final DeliveryClient deliveryClient;
    private final AddressRepository addressRepository;
    private final OrderMapper orderMapper;
    private final PaymentClient paymentClient;


    public List<OrderDto> gerOrderByUserName(String userId) {
        return orderRepository.findByUserName(userId).stream().map(orderMapper::toOrderDto).toList();
    }

    @Transactional
    public OrderDto createOrder(CreateNewOrderRequest request) {
        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .state(State.NEW)
                .userName(UUID.randomUUID().toString())
                .products(request.getShoppingCart().getProducts())
                .build();
        Address address = orderMapper.toAddress(request.getDeliveryAddress());
        address.setAddressId(address.getCountry());
        if (!addressRepository.existsById(address.getAddressId())) {
            address = addressRepository.save(address);
        }
        order.setAddress(address);
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    @Transactional
    public void returnOrder(ProductReturnRequest request) {
        Order order = getOrder(request.getOrderId());
        wareHouseClient.returnProducts(request.getProducts());
        order.setState(State.PRODUCT_RETURNED);
        orderRepository.save(order);

    }

    @Transactional
    public void successfulDelivery(String orderId) {
        Order order = getOrder(orderId);
        order.setState(State.DELIVERED);
        orderRepository.save(order);

    }

    @Transactional
    public void failedDelivery(String orderId) {
        Order order = getOrder(orderId);
        order.setState(State.DELIVERY_FAILED);
        orderRepository.save(order);
    }

    @Transactional
    public void successfulPayment(String orderId) {
        Order order = getOrder(orderId);
        order.setState(State.PAID);
        orderRepository.save(order);
    }

    @Transactional
    public void failedPayment(String orderId) {
        Order order = getOrder(orderId);
        order.setState(State.PAYMENT_FAILED);
        orderRepository.save(order);
    }

    @Transactional
    public OrderDto assemblyOrder(String orderId) {
        Order order = getOrder(orderId);
        BookedProductDto bookedProduct = wareHouseClient
                .assemblyOrder(AssemblyProductsForOrderRequest.builder()
                        .orderId(order.getOrderId()).products(order.getProducts()).build());
        order.setDeliveryVolume(bookedProduct.getDeliveryVolume());
        order.setDeliveryWeight(bookedProduct.getDeliveryWeight());
        order.setFragile(bookedProduct.getFragile());
        order.setState(State.ASSEMBLED);
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    public OrderDto assemblyFailed(String orderId) {
        Order order = getOrder(orderId);
        order.setState(State.ASSEMBLY_FAILED);
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto calculateTotal(String orderId) {
        Order order = getOrder(orderId);
        order.setProductPrice(paymentClient.calculateCost(orderMapper.toOrderDto(order)));
        order.setTotalPrice(paymentClient.calculateTotalCost(orderMapper.toOrderDto(order)));
        order.setPaymentId(paymentClient.createPayment(orderMapper.toOrderDto(order)).getPaymentId());
        return orderMapper.toOrderDto(orderRepository.save(order));

    }

    @Transactional
    public OrderDto calculateDelivery(String orderId) {
        AddressDto addressFrom = wareHouseClient.getAddress();
        Order order = getOrder(orderId);
        AddressDto addressTo = orderMapper.toAddressDto(order.getAddress());
        DeliveryDto deliveryDto = deliveryClient.createDelivery(DeliveryDto.builder()
                .fromAddress(addressFrom)
                .toAddress(addressTo)
                .deliveryState(DeliveryState.CREATED)
                .orderId(order.getOrderId())
                .build());
        order.setDeliveryId(deliveryDto.getDeliveryId());
        order.setDeliveryPrice(deliveryClient.calculateDelivery(orderMapper.toOrderDto(order)));
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto completeOrder(String orderId) {
        Order order = getOrder(orderId);
        order.setState(State.COMPLETED);
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    private Order getOrder(String orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказа с таким id нет"));
    }
}
