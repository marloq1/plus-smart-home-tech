package ru.yandex.practicum;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.dto.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Product,String> {

    List<Product> findByProductCategory(ProductCategory category, Pageable pageable);

    Optional<Product> findByProductId(String productId);
}
