package ru.yandex.practicum;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/warehouse")
@RequiredArgsConstructor
public class WareHouseController {

    private final WareHouseService wareHouseService;

    @PutMapping
    public void addProductToWareHouse(@RequestBody @Valid NewProductInWarehouseRequest request) {
        wareHouseService.addProductToWareHouse(request);
    }

    @PostMapping("/add")
    public void addProductQuantity(@RequestBody AddProductToWareHouseRequest request) {
        wareHouseService.addProductQuantity(request);
    }

    @PostMapping("/check")
    public BookedProductDto checkProducts(@RequestBody ShoppingCartDto shoppingCartDto) {
        return wareHouseService.checkProducts(shoppingCartDto);
    }

    @GetMapping("/address")
    public AddressDto getAddress() {
        return new AddressDto();
    }

    @PostMapping("/assembly")
    public BookedProductDto assemblyOrder(@RequestBody AssemblyProductsForOrderRequest request) {
        return wareHouseService.assemblyOrder(request);
    }

    @PostMapping("/shipped")
    public void shippedToDelivery(@RequestBody ShippedToDeliveryRequest request) {
        wareHouseService.shippedToDelivery(request);
    }

    @PostMapping("/return")
    public void returnProducts(@RequestBody Map<String, Long> products) {
        wareHouseService.returnProducts(products);
    }

}
