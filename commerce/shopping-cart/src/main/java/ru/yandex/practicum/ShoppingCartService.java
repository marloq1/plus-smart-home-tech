package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.CartNotFoundException;
import ru.yandex.practicum.exception.DeactivatedException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.exception.ProductIdNotFoundException;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.feign.WareHouseClient;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCartMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final WareHouseClient wareHouseClient;

    @Transactional
    public ShoppingCartDto addProductsToCart(String userName, Map<String, Long> products) {
        wareHouseClient.checkProducts(ShoppingCartDto.builder().products(products).build());
        Optional<ShoppingCart> cartFromDb = getCartFromDb(userName);
        if (cartFromDb.isPresent()) {
            checkState(cartFromDb.get());
            Map<String, Long> productsFromDb = cartFromDb.get().getProducts();
            productsFromDb.putAll(products);
            return shoppingCartMapper.cartToDto(shoppingCartRepository.save(cartFromDb.get()));

        } else {
            ShoppingCart shoppingCart = shoppingCartMapper.paramsToCart(userName, products);
            shoppingCart.setCartState(true);
            return shoppingCartMapper.cartToDto(shoppingCartRepository.save(shoppingCart));
        }
    }

    public ShoppingCartDto getCart(String userName) {
        return shoppingCartMapper.cartToDto(getCartFromDb(userName)
                .orElseThrow(() -> new CartNotFoundException("У пользователя с этим id нет корзины")));
    }

    @Transactional
    public void deactivateCart(String userName) {
        Optional<ShoppingCart> cartFromDb = getCartFromDb(userName);
        if (cartFromDb.isPresent()) {
            cartFromDb.get().setCartState(false);
            shoppingCartRepository.save(cartFromDb.get());
        } else {
            throw new CartNotFoundException("У пользователя с этим id нет корзины");
        }

    }

    @Transactional
    public ShoppingCartDto changeProductsInCart(String userName, List<String> productIds) {
        Optional<ShoppingCart> cartFromDb = getCartFromDb(userName);

        if (cartFromDb.isPresent()) {
            Map<String, Long> products = cartFromDb.get().getProducts();
            checkState(cartFromDb.get());
            for (Map.Entry<String, Long> entry : cartFromDb.get().getProducts().entrySet()) {
                for (String id : productIds) {
                    if (id.equals(entry.getKey())) {
                        products.remove(id);
                    }
                }
            }
            cartFromDb.get().setProducts(products);
            return shoppingCartMapper.cartToDto(shoppingCartRepository.save(cartFromDb.get()));
        } else {
            throw new CartNotFoundException("У пользователя с этим id нет корзины");
        }
    }

    @Transactional
    public ShoppingCartDto changeProductsQuantity(String userName, ChangeProductQuantityRequest request) {
        Optional<ShoppingCart> cartFromDb = getCartFromDb(userName);
        if (cartFromDb.isPresent()) {
            checkState(cartFromDb.get());
            if (cartFromDb.get().getProducts().get(request.getProductId()) != null) {
                wareHouseClient.checkProducts(ShoppingCartDto.builder()
                        .products(Map.of(request.getProductId(), request.getNewQuantity())).build());
                cartFromDb.get().getProducts().put(request.getProductId(), request.getNewQuantity());
                return shoppingCartMapper.cartToDto(shoppingCartRepository.save(cartFromDb.get()));
            } else {
                throw new ProductIdNotFoundException("Товара с таким id не найдено");
            }
        } else {
            throw new CartNotFoundException("У пользователя с этим id нет корзины");
        }
    }

    private Optional<ShoppingCart> getCartFromDb(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new NotAuthorizedUserException("Необходимо задать пользователя");
        }
        return shoppingCartRepository.findByUserName(userName);
    }

    private void checkState(ShoppingCart cart) {
        if (!cart.getCartState()) {
            throw new DeactivatedException("Нельзя изменять деактивированную корзину");
        }
    }
}
