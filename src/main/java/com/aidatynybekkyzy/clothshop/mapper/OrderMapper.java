package com.aidatynybekkyzy.clothshop.mapper;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "orderItems", target = "items")
    OrderDto toDto(Order order);

    @Mapping(source = "items", target = "orderItems")
    Order toEntity(OrderDto orderDto);

    List<OrderDto> toDtoList(List<Order> order);

}
