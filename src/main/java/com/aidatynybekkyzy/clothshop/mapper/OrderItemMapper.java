package com.aidatynybekkyzy.clothshop.mapper;

import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "id", target = "id")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(source = "productId", target = "productId")
    OrderItem toEntity(OrderItemDto orderItemDto);

    Set<OrderItemDto> toDtoSet(Set<OrderItem> orderItems);
    Set<OrderItem> toEntitySet(Set<OrderItemDto> orderItemDtos);
}
