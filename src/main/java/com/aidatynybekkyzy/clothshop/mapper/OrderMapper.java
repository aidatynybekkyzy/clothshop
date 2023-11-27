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
public interface OrderMapper {
    @Mapping(source = "orderItems", target = "items")
    OrderDto toDto(Order order);
    @Mapping(source = "id", target = "id")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(source = "items", target = "orderItems")
    Order toEntity(OrderDto orderDto);

    List<OrderDto> toDtoList(List<Order> order);
    Set<OrderItemDto> toDtoSet(Set<OrderItem> orderItems);

}
