package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.*;

import java.util.Map;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WareHouseClient {


    @PostMapping("/check")
    BookedProductDto checkProducts(@RequestBody ShoppingCartDto shoppingCartDto);

    @PostMapping("/assembly")
    BookedProductDto assemblyOrder(@RequestBody AssemblyProductsForOrderRequest request);

    @GetMapping("/address")
    AddressDto getAddress();

    @PostMapping("/shipped")
    void shippedToDelivery(@RequestBody ShippedToDeliveryRequest request);

    @PostMapping("/return")
    void returnProducts(@RequestBody Map<String, Long> products);

}
