package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.JsonUtils;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.exception.exceptionHandler.GlobalExceptionHandler;
import com.aidatynybekkyzy.clothshop.repository.ProductRepository;
import com.aidatynybekkyzy.clothshop.repository.VendorRepository;
import com.aidatynybekkyzy.clothshop.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    ProductControllerTest(VendorRepository vendorRepository, ProductRepository productRepository) {
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
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
                        .content(JsonUtils.asJsonString(productDto)))
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
        ProductDto productDto = createValidProductDto();
        productDto.setName(null);
        when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(productDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create Product - Empty Product Name")
    public void createProduct_emptyProductName() throws Exception {
        ProductDto productDto = createValidProductDto();
        productDto.setName("");

        when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);

        mockMvc.perform(post("/products/admin/createProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(productDto)))
                .andExpect(status().isBadRequest());
        assertEquals(productDto.getName(), "");

        verifyNoInteractions(productService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create Product - Product with Same Name Already Exists")
    public void createProduct_productNameAlreadyExists() throws Exception {
        ProductDto productDto = createValidProductDto();

        when(productRepository.existsByName(productDto.getName())).thenReturn(true);

        mockMvc.perform(post("/products/admin/createProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(productDto)))
                .andExpect(status().isConflict());

        verifyNoInteractions(productService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create Product - Vendor Not Found")
    public void createProduct_vendorNotFound() throws Exception {
        ProductDto productDto = createValidProductDto();

        when(vendorRepository.findById(productDto.getVendorId())).thenReturn(Optional.empty());

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(productDto)))
                .andExpect(status().isNotFound());

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
}