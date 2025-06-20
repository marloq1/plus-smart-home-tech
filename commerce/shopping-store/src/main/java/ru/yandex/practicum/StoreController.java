package ru.yandex.practicum;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PageResponse;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;



@RestController
@RequestMapping(path = "api/v1/shopping-store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;


    @GetMapping
    public PageResponse<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable) {
        return storeService.getProducts(category, pageable);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable String productId) {
        return storeService.getProduct(productId);
    }

    @PutMapping
    public ProductDto saveProduct(@RequestBody @Valid ProductDto productDto) {
        return storeService.saveProduct(productDto);
    }

    @PostMapping
    public ProductDto changeProduct(@RequestBody ProductDto productDto) {
        return storeService.changeProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public boolean removeProduct(@RequestBody String productId) {
        return storeService.removeProduct(productId.replaceAll("^\"|\"$", ""));
    }

    @PostMapping("/quantityState")
    public boolean setQuantityState(@ModelAttribute SetProductQuantityStateRequest request) {
        return storeService.setQuantityState(request);
    }

}
