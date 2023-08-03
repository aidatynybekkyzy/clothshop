package com.aidatynybekkyzy.clothshop.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @CreationTimestamp
    private LocalDateTime shipDate;

    @NotNull
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(length = 30)
    @Builder.Default
    private String status = OrderStatus.PLACED.name();

    @NotNull
    @Column(name = "complete")
    @Builder.Default
    private Boolean complete = false;

    @NotEmpty
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    @JsonManagedReference
    private Set<OrderItem> orderItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "user_order",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id"))
    private User user;

    public Order() {
        this.orderItems = new HashSet<>();
    }

    public void add(OrderItem orderItem) {
        if (orderItem != null) {
            if (orderItems == null) {
                orderItems = new HashSet<>();
            }
            orderItems.add(orderItem);
            orderItem.setOrder(this);

        }
    }

    public boolean remove(Long orderItemId) {

        if (orderItems != null) {

            for (OrderItem oi : orderItems) {
                if (Objects.equals(oi.getId(), orderItemId))
                    return orderItems.remove(oi);
            }
        }

        return false;
    }

    public BigDecimal getTotalPrice() {
        return orderItems.stream()
                .map(OrderItem::getSellingPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "Order{" +
                "shipDate=" + shipDate +
                ", createdAt=" + createdAt +
                ", status='" + status + '\'' +
                ", complete=" + complete +
                ", items=" + orderItems +
                ", user=" + user +
                '}';
    }
}
