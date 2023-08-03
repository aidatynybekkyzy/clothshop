package com.aidatynybekkyzy.clothshop.mapper;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.model.Order;
import com.aidatynybekkyzy.clothshop.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "productId", target = "productId")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(source = "productId", target = "productId")
    OrderItem toEntity(OrderItemDto orderItemDto);

    List<OrderDto> toDtoList(List<Order> order);

}
