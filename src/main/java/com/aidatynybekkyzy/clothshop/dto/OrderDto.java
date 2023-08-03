package com.aidatynybekkyzy.clothshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    @NotNull
    private Set<OrderItemDto> items;
}
