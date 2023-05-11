package com.aidatynybekkyzy.clothshop.repository;

import com.aidatynybekkyzy.clothshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
