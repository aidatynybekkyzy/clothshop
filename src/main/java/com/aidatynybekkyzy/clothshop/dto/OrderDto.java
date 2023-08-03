package com.aidatynybekkyzy.clothshop.dto;

import com.aidatynybekkyzy.clothshop.model.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private LocalDateTime shipDate;
    private LocalDateTime createdAt;
    private String status;
    private Boolean complete;
    @NotNull
    private Set<OrderItem> items;
    @JsonIgnore
    private Long userId;

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", shipDate=" + shipDate +
                ", createdAt=" + createdAt +
                ", status='" + status + '\'' +
                ", complete=" + complete +
                ", items=" + items +
                '}';
    }
}
