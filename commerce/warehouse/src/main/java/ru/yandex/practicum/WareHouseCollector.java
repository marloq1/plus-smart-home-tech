package ru.yandex.practicum;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;

@RestController
@RequestMapping(path = "/api/v1/warehouse")
@RequiredArgsConstructor
public class WareHouseCollector {

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

}
