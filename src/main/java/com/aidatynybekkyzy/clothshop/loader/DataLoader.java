package com.aidatynybekkyzy.clothshop.loader;

import com.aidatynybekkyzy.clothshop.model.*;
import com.aidatynybekkyzy.clothshop.enums.OrderStatus;
import com.aidatynybekkyzy.clothshop.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(ProductRepository productRepository,
                      VendorRepository vendorRepository,
                      CategoryRepository categoryRepository,
                      OrderRepository orderRepository,
                      UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args){
        loadData();
    }

    private void loadData() {
        Role roleAdmin = new Role();
        roleAdmin.setRoleName("ADMIN");

        Role roleUser = new Role();
        roleUser.setRoleName("USER");

        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);

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

        User admin = new User();
        admin.setId(1L);
        admin.setUsername("aidaTyn");
        admin.setFirstName("Aida");
        admin.setLastName("Tynybek kyzy");
        admin.setEmail("aidatyn@gmail.com");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setPhone("0555001001");
        admin.setRole(Set.of(roleAdmin));
        userRepository.save(admin);

        User user = new User();
        user.setId(2L);
        user.setUsername("username");
        user.setFirstName("User");
        user.setLastName("User Last Name");
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setPhone("0777001001");
        user.setRole(Set.of(roleUser));
        userRepository.save(user);

        Order order = new Order();
        order.setId(1L);
        order.setShipDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED.name());
        order.setComplete(true);
        order.setUser(user);
        orderRepository.save(order);

        OrderItem item1 = OrderItem.builder()
                .productId(1L)
                .quantity(12)
                .sellingPrice(new BigDecimal(1500))
                .order(order)
                .build();
        item1.setId(1L);

        order.setOrderItems(Set.of(item1));
        user.setOrders(Set.of(order));
        userRepository.save(user);

    }
}