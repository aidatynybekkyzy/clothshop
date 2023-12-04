package com.aidatynybekkyzy.clothshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    @NotNull @JsonIgnore
    private Long id;
    @JsonIgnore
    private LocalDateTime shipDate;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private String status;
    @JsonIgnore
    private Boolean complete;
    @NotNull
    private Set<OrderItemDto> items;
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
