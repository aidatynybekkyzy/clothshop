package com.aidatynybekkyzy.clothshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private LocalDateTime shipDate;
    private LocalDateTime createdAt;
    private String status;
    private Boolean complete;
    private List<ProductDto> items;
    @JsonIgnore
    private Long userId;
}
