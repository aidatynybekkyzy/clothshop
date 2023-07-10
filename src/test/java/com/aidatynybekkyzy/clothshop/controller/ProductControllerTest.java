package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.TestCacheConfig;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.repository.ProductRepository;
import com.aidatynybekkyzy.clothshop.repository.VendorRepository;
import com.aidatynybekkyzy.clothshop.service.impl.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ContextConfiguration(classes = TestCacheConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class ProductControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductServiceImpl productService;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private static final Long VENDOR_ID = 1L;
    private static final Long PRODUCT_ID = 1L;
    private static final String PRODUCT_NAME = "Test Product";
    private static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(10.99);
    private static final Integer PRODUCT_QUANTITY = 5;
    private static final Long PRODUCT_CATEGORY_ID = 1L;

    ProductControllerTest(VendorRepository vendorRepository, ProductRepository productRepository) {
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create Product - Success")
    public void createProduct_success() throws Exception {
        ProductDto productDto = createValidProductDto();

        ProductDto createdProductDto = new ProductDto();
        createdProductDto.setId(PRODUCT_ID);
        createdProductDto.setName(PRODUCT_NAME);
        createdProductDto.setPrice(PRODUCT_PRICE);
        createdProductDto.setQuantity(PRODUCT_QUANTITY);
        createdProductDto.setCategoryId(PRODUCT_CATEGORY_ID);
        createdProductDto.setVendorId(VENDOR_ID);

        when(productService.createProduct(any(ProductDto.class))).thenReturn(createdProductDto);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(PRODUCT_ID))
                .andExpect(jsonPath("$.name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.price").value(PRODUCT_PRICE))
                .andExpect(jsonPath("$.quantity").value(PRODUCT_QUANTITY))
                .andExpect(jsonPath("$.categoryId").value(PRODUCT_CATEGORY_ID))
                .andExpect(jsonPath("$.vendorId").value(VENDOR_ID));

        verify(productService, times(1)).createProduct(any(ProductDto.class));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create Product - Missing Product Name")
    public void createProduct_missingProductName() throws Exception {
        // Test data
        ProductDto productDto = createValidProductDto();
        productDto.setName(null);

        // Perform the request and expect status 400 (BAD REQUEST)
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productDto)))
                .andExpect(status().isBadRequest());

        // Verify that the service method was not called
        verifyNoInteractions(productService);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create Product - Empty Product Name")
    public void createProduct_emptyProductName() throws Exception {
        // Test data
        ProductDto productDto = createValidProductDto();
        productDto.setName("");

        // Perform the request and expect status 400 (BAD REQUEST)
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productDto)))
                .andExpect(status().isBadRequest());

        // Verify that the service method was not called
        verifyNoInteractions(productService);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create Product - Product with Same Name Already Exists")
    public void createProduct_productNameAlreadyExists() throws Exception {
        // Test data
        ProductDto productDto = createValidProductDto();

        // Mock the productRepository.existsByName() to return true (product with same name already exists)
        when(productRepository.existsByName(productDto.getName())).thenReturn(true);

        // Perform the request and expect status 409 (CONFLICT)
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productDto)))
                .andExpect(status().isConflict());

        // Verify that the service method was not called
        verifyNoInteractions(productService);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create Product - Vendor Not Found")
    public void createProduct_vendorNotFound() throws Exception {
        // Test data
        ProductDto productDto = createValidProductDto();

        // Mock the vendorRepository.findById() to return Optional.empty() (vendor not found)
        when(vendorRepository.findById(productDto.getVendorId())).thenReturn(Optional.empty());

        // Perform the request and expect status 404 (NOT FOUND)
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productDto)))
                .andExpect(status().isNotFound());

        // Verify that the service method was not called
        verifyNoInteractions(productService);
    }

    @Test
    void getProduct() {
    }

    @Test
    void getAllProducts() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void getProductPhoto() {
    }

    @Test
    void deleteProduct() {
    }

    @Test
    void addPhoto() {
    }
    private ProductDto createValidProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setName(PRODUCT_NAME);
        productDto.setPrice(PRODUCT_PRICE);
        productDto.setQuantity(PRODUCT_QUANTITY);
        productDto.setCategoryId(PRODUCT_CATEGORY_ID);
        productDto.setVendorId(VENDOR_ID);
        return productDto;
    }

    private String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}