package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto getCart(@RequestParam(name = "username", required = false) String userName) {
        return shoppingCartService.getCart(userName);
    }

    @PutMapping
    public ShoppingCartDto addProductsToCart(@RequestParam(name = "username", required = false) String userName,
                                             @RequestBody Map<String, Long> products) {
        return shoppingCartService.addProductsToCart(userName, products);
    }

    @DeleteMapping
    public void deactivateCart(@RequestParam(name = "username", required = false) String userName) {
        shoppingCartService.deactivateCart(userName);
    }

    @PostMapping("/remove")
    public ShoppingCartDto changeProductsInCart(@RequestParam(name = "username", required = false) String userName,
                                                @RequestBody List<String> productIds) {

        return shoppingCartService.changeProductsInCart(userName, productIds);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductsQuantity(@RequestParam(name = "username", required = false) String userName,
                                                  @RequestBody ChangeProductQuantityRequest request) {

        return shoppingCartService.changeProductsQuantity(userName, request);
    }


}
