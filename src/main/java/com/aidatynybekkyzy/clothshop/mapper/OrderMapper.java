package com.aidatynybekkyzy.clothshop.mapper;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.model.Order;
import com.aidatynybekkyzy.clothshop.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "id", target = "id")
    OrderDto toDto(Order order);

    @Mapping(source = "id", target = "id")
    Order toEntity(OrderDto orderDto);

    List<OrderDto> toDtoList(List<Order> order);

    Product toProductEntity(ProductDto productDto);

    ProductDto toItemDto(Product product);
    Set<ProductDto> toItemDtoSet(Set<Product> orderItems);
}
