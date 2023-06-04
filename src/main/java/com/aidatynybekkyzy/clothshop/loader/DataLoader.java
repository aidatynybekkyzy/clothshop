package com.aidatynybekkyzy.clothshop.loader;

import com.aidatynybekkyzy.clothshop.model.*;
import com.aidatynybekkyzy.clothshop.repository.*;
import com.aidatynybekkyzy.clothshop.service.CategoryService;
import com.aidatynybekkyzy.clothshop.service.ProductService;
import com.aidatynybekkyzy.clothshop.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public DataLoader(UserService userService, CategoryService categoryService, ProductService productService,
                      ProductRepository productRepository,
                      VendorRepository vendorRepository,
                      CategoryRepository categoryRepository,
                      OrderRepository orderRepository,
                      UserRepository userRepository) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.productRepository = productRepository;
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {
        Product product = new Product();
        product.setId(1L);
        product.setName("T-Shirt");
        product.setPrice(new BigDecimal(1500));
        product.setQuantity(3);
        product.setCategoryId(1L);
        product.setVendorId(1L);
        productRepository.save(product);

        Vendor vendor = new Vendor();
        vendor.setVendorId(1L);
        vendor.setVendorName("Polo");
        vendor.setProducts(List.of(product));
        vendorRepository.save(vendor);

        Category menClothing = new Category();
        menClothing.setId(1L);
        menClothing.setCategoryName("Men's clothing");
        menClothing.setProducts(List.of(product));
        categoryRepository.save(menClothing);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProduct(product);
        orderItem.setQuantity(3);
        orderItem.setPrice(new BigDecimal(1000));

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
        order.setOrderItems(List.of(orderItem));
        order.setUser(user);
        orderRepository.save(order);


        user.setOrders(Set.of(order));
        userRepository.save(user);

    }
}
