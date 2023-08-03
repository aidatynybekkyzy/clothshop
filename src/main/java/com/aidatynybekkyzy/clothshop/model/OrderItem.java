package com.aidatynybekkyzy.clothshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@Table(name = "orderItems")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long productId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    private int quantity;

    @Column(name="selling_price")
    private BigDecimal sellingPrice;

    public OrderItem() {

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
