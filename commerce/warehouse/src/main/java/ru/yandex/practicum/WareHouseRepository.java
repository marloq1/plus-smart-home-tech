package ru.yandex.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.WareHouseProduct;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WareHouseRepository extends JpaRepository<WareHouseProduct,String> {

    Optional<WareHouseProduct> findByProductId(String productId);

    List<WareHouseProduct> findByProductIdIn(Set<String> ids);
}
