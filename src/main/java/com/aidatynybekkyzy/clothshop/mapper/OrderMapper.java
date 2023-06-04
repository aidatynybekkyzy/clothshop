package com.aidatynybekkyzy.clothshop.mapper;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.model.Order;
import com.aidatynybekkyzy.clothshop.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "id", target = "id")
    OrderDto toDto(Order order);

    @Mapping(source = "id", target = "id")
    Order toEntity(OrderDto orderDto);

    List<OrderDto> toDtoList(List<Order> order);

    OrderItem toOrderItemEntity(OrderItemDto orderItemDto);

    OrderItemDto toOrderItemDto(OrderItem orderItem);
    List<OrderItemDto> toOrderItemDtoList(List<OrderItem> orderItems);
}
