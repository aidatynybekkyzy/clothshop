package com.aidatynybekkyzy.clothshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private List<Product> items;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

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
