package com.aidatynybekkyzy.clothshop.repository;

import com.aidatynybekkyzy.clothshop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
