package ru.yandex.practicum.model;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {

    ShoppingCartDto cartToDto(ShoppingCart shoppingCart);

    default ShoppingCart paramsToCart(String userName, Map<String, Long> products) {
        return ShoppingCart.builder()
                .userName(userName)
                .products(products)
                .build();
    }
}
