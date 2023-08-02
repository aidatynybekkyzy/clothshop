package com.aidatynybekkyzy.clothshop.model;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Order extends AbstractEntity<Long> {
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
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Product> items = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "user_order",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id"))
    private User user;

    public void  add (Product orderItem){
        if (orderItem != null){
            if (items == null){
                items = new HashSet<>();
            }
            items.add(orderItem);
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "shipDate=" + shipDate +
                ", createdAt=" + createdAt +
                ", status='" + status + '\'' +
                ", complete=" + complete +
                ", items=" + items +
                ", user=" + user +
                '}';
    }
}
