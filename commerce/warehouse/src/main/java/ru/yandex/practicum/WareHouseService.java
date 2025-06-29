package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.DuplicateProductException;
import ru.yandex.practicum.exception.ProductIdNotFoundException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.model.OrderDelivery;
import ru.yandex.practicum.model.OrderDeliveryId;
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
    private final OrderClient orderClient;
    private final OrderDeliveryRepository orderDeliveryRepository;


    @Transactional
    public void addProductToWareHouse(NewProductInWarehouseRequest request) {
        if (getProduct(request.getProductId()).isEmpty()) {
            WareHouseProduct wareHouseProduct = wareHouseMapper.toEntity(request);
            wareHouseProduct.setQuantity(1L);
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
        List<WareHouseProduct> productsFromDb = wareHouseRepository.findByProductIdIn(shoppingCartDto
                .getProducts().keySet());
        return check(shoppingCartDto.getProducts(), productsFromDb);
    }

    @Transactional
    public BookedProductDto assemblyOrder(AssemblyProductsForOrderRequest request) {
        List<WareHouseProduct> productsFromDb = wareHouseRepository.findByProductIdIn(request.getProducts().keySet());
        BookedProductDto bookedProduct;
        try {
            bookedProduct = check(request.getProducts(), productsFromDb);
        } catch (ProductInShoppingCartLowQuantityInWarehouseException e) {
            orderClient.assemblyFailed(request.getOrderId());
            throw new ProductInShoppingCartLowQuantityInWarehouseException("Недостаточно товара на складе");
        }
        for (Map.Entry<String, Long> entry : request.getProducts().entrySet()) {
            for (WareHouseProduct product : productsFromDb) {
                if (product.getProductId().equals(entry.getKey())) {
                    product.setQuantity(product.getQuantity() - entry.getValue());
                }
            }
        }
        wareHouseRepository.saveAll(productsFromDb);
        return bookedProduct;
    }

    @Transactional
    public void returnProducts(Map<String, Long> products) {
        List<WareHouseProduct> productsFromDb = wareHouseRepository.findByProductIdIn(products.keySet());
        for (Map.Entry<String, Long> entry : products.entrySet()) {
            for (WareHouseProduct product : productsFromDb) {
                if (product.getProductId().equals(entry.getKey())) {
                    product.setQuantity(product.getQuantity() + entry.getValue());
                }
            }
        }
        wareHouseRepository.saveAll(productsFromDb);
    }

    @Transactional
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        OrderDeliveryId id = new OrderDeliveryId(request.getOrderId(), request.getDeliveryId());
        OrderDelivery orderDelivery = new OrderDelivery();
        orderDelivery.setId(id);
        orderDeliveryRepository.save(orderDelivery);
    }

    private BookedProductDto check(Map<String, Long> products, List<WareHouseProduct> productsFromDb) {
        Double deliveryWeight = 0.0;
        Double deliveryVolume = 0.0;
        Boolean fragile = false;

        if (productsFromDb == null || !(productsFromDb.size() == products.keySet().size())) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException("Недостаточно товара на складе");
        }
        for (Map.Entry<String, Long> entry : products.entrySet()) {
            for (WareHouseProduct product : productsFromDb) {
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
