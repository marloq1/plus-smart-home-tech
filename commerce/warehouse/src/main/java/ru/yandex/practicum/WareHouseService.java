package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.DuplicateProductException;
import ru.yandex.practicum.exception.ProductIdNotFoundException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.model.WareHouseProduct;
import ru.yandex.practicum.model.WareHouseMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WareHouseService {

    private final WareHouseMapper wareHouseMapper;
    private final WareHouseRepository wareHouseRepository;


    @Transactional
    public void addProductToWareHouse(NewProductInWarehouseRequest request) {
        if (getProduct(request.getProductId()).isEmpty()) {
            WareHouseProduct wareHouseProduct = wareHouseMapper.toEntity(request);
            wareHouseProduct.setQuantity(1);
            wareHouseRepository.save(wareHouseProduct);
        } else {
            throw new DuplicateProductException("Предмет с таким id уже есть на складе");
        }
    }

    @Transactional
    public void addProductQuantity(AddProductToWareHouseRequest request) {
        Optional<WareHouseProduct> wareHouseProduct = getProduct(request.getProductId());
        if (wareHouseProduct.isPresent()) {
            wareHouseProduct.get().setQuantity(wareHouseProduct.get().getQuantity() + request.getQuantity());
            wareHouseRepository.save(wareHouseProduct.get());
        } else {
            throw new ProductIdNotFoundException("Предмета с таким id нет на складе");
        }
    }

    public BookedProductDto checkProducts(ShoppingCartDto shoppingCartDto) {
        List<WareHouseProduct> products = wareHouseRepository.findByProductIdIn(shoppingCartDto.getProducts().keySet());
        Double deliveryWeight = 0.0;
        Double deliveryVolume = 0.0;
        Boolean fragile = false;

        if (products == null || !(products.size() == shoppingCartDto.getProducts().keySet().size())) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException("Недостаточно товара на складе");
        }
        for (Map.Entry<String, Long> entry : shoppingCartDto.getProducts().entrySet()) {
            for (WareHouseProduct product : products) {
                if (product.getProductId().equals(entry.getKey())) {
                    if (product.getQuantity() >= entry.getValue()) {
                        deliveryWeight += product.getWeight();
                        deliveryVolume += (product.getWidth() * product.getHeight() * product.getDepth());
                        if (product.getFragile()) {
                            fragile = true;
                        }
                    } else {
                        throw new ProductInShoppingCartLowQuantityInWarehouseException("Недостаточно товара на складе");
                    }
                }
            }
        }
        return BookedProductDto.builder()
                .deliveryVolume(deliveryVolume)
                .deliveryWeight(deliveryWeight)
                .fragile(fragile)
                .build();
    }

    private Optional<WareHouseProduct> getProduct(String productId) {
        return wareHouseRepository.findByProductId(productId);
    }
}
