package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.PageResponse;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SortField;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.dto.ProductState;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.model.ProductMapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final ProductMapper productMapper;


    public PageResponse<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        List<ProductDto> products = storeRepository.findByProductCategory(category, pageable)
                .stream()
                .map(productMapper::productToDto)
                .toList();
        List<SortField> sortFields = pageable.getSort()
                .stream()
                .map(order -> new SortField(order.getProperty(), order.getDirection().name()))
                .toList();
        return new PageResponse<>(products, sortFields);

    }

    public ProductDto getProduct(String productId) {
        return productMapper.productToDto(storeRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Продукта с таким id нет")));
    }

    @Transactional
    public ProductDto saveProduct(ProductDto productDto) {
        if (productDto.getProductId() == null) {
            productDto.setProductId(UUID.randomUUID().toString());
        }
        return productMapper.productToDto(storeRepository.save(productMapper.dtoToProduct(productDto)));
    }


    @Transactional
    public ProductDto changeProduct(ProductDto productDto) {
        ProductDto productFromDb = getProduct(productDto.getProductId());
        if (productDto.getProductName() != null) {
            productFromDb.setProductName(productDto.getProductName());
        }
        if (productDto.getProductState() != null) {
            productFromDb.setProductState(productDto.getProductState());
        }
        if (productDto.getProductCategory() != null) {
            productFromDb.setProductCategory(productDto.getProductCategory());
        }
        if (productDto.getDescription() != null) {
            productFromDb.setDescription(productDto.getDescription());
        }
        if (productDto.getQuantityState() != null) {
            productFromDb.setQuantityState(productDto.getQuantityState());
        }
        if (productDto.getImageSrc() != null) {
            productFromDb.setImageSrc(productDto.getImageSrc());
        }
        if (productDto.getPrice() != null) {
            productFromDb.setPrice(productDto.getPrice());
        }
        return saveProduct(productFromDb);
    }

    @Transactional
    public boolean removeProduct(String productId) {
        ProductDto productFromDb = getProduct(productId);
        productFromDb.setProductState(ProductState.DEACTIVATE);
        return saveProduct(productFromDb) != null;
    }

    @Transactional
    public boolean setQuantityState(SetProductQuantityStateRequest request) {
        ProductDto productFromDb = getProduct(request.getProductId());
        if (request.getQuantityState() != null) {
            productFromDb.setQuantityState(request.getQuantityState());
            return saveProduct(productFromDb) != null;
        } else
            return false;

    }
}
