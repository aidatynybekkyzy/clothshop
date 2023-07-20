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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id != null && id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
