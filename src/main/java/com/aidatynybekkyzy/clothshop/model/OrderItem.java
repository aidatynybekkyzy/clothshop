package com.aidatynybekkyzy.clothshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orderItems")
public class OrderItem  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long productId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    private int quantity;

    @Column(name="selling_price")
    private BigDecimal sellingPrice;
    @Tolerate
    public OrderItem(Long id, Long productId, int quantity, BigDecimal sellingPrice) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", productId=" + productId +
                ", order=" + order +
                ", quantity=" + quantity +
                ", sellingPrice=" + sellingPrice +
                '}';
    }
}
