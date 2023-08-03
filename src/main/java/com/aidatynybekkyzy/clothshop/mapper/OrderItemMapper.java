package com.aidatynybekkyzy.clothshop.mapper;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.model.Order;
import com.aidatynybekkyzy.clothshop.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "productId", target = "productId")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(source = "productId", target = "productId")
    OrderItem toEntity(OrderItemDto orderItemDto);

    Set<OrderItemDto> toDtoSet(Set<OrderItem> orderItems);
    Set<OrderItem> toEntitySet(Set<OrderItemDto> orderItemDtos);
}
