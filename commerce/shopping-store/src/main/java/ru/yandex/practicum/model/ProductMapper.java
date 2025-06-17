package ru.yandex.practicum.model;


import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.ProductDto;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto productToDto(Product product);

    Product dtoToProduct(ProductDto productDto);
}
