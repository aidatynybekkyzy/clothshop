package com.aidatynybekkyzy.clothshop.loader;

import com.aidatynybekkyzy.clothshop.model.*;
import com.aidatynybekkyzy.clothshop.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public DataLoader(ProductRepository productRepository,
                      VendorRepository vendorRepository,
                      CategoryRepository categoryRepository,
                      OrderRepository orderRepository,
                      UserRepository userRepository) {
        this.productRepository = productRepository;
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args){
        loadData();
    }

    private void loadData() {
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        vendor.setVendorName("Polo");
        vendor.setProducts(new ArrayList<>());
        vendorRepository.save(vendor);

        Category category = new Category();
        category.setId(1L);
        category.setCategoryName("Men's clothing");
        category.setProducts(new ArrayList<>());
        categoryRepository.save(category);

        Product product = new Product();
        product.setId(1L);
        product.setName("T-Shirt");
        product.setPrice(new BigDecimal(1500));
        product.setQuantity(3);
        product.setCategoryId(category.getId());
        product.setVendorId(vendor.getId());
        productRepository.save(product);

        User user = new User();
        user.setId(1L);
        user.setUsername("aidaTyn");
        user.setFirstName("Aida");
        user.setLastName("Tynybek kyzy");
        user.setEmail("aidatyn@gmail.com");
        user.setPassword("password");
        user.setPhone("0555001001");
        userRepository.save(user);

        Order order = new Order();
        order.setId(1L);
        order.setShipDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED.name());
        order.setComplete(true);
        order.setItems(Set.of(product));
        order.setUser(user);
        orderRepository.save(order);

        user.setOrders(Set.of(order));
        userRepository.save(user);

    }
}
