package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.JsonUtils;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.exception.EntityNotFoundException;
import com.aidatynybekkyzy.clothshop.exception.InvalidArgumentException;
import com.aidatynybekkyzy.clothshop.exception.exceptionhandler.GlobalExceptionHandler;
import com.aidatynybekkyzy.clothshop.service.common.ResponseErrorValidation;
import com.aidatynybekkyzy.clothshop.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class ProductControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private ProductController productController;
    @Mock
    private ResponseErrorValidation responseErrorValidation;
    @Mock
    private ProductServiceImpl productService;
    private static final Long VENDOR_ID = 1L;
    private static final Long PRODUCT_ID = 1L;
    private static final String PRODUCT_NAME = "Test Product";
    private static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(10.99);
    private static final Integer PRODUCT_QUANTITY = 5;
    private static final Long PRODUCT_CATEGORY_ID = 1L;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController, responseErrorValidation)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create Product - Success")
     void createProduct_success() throws Exception {
        ProductDto productDto = createValidProductDto();

        ProductDto createdProductDto = new ProductDto();
        createdProductDto.setId(PRODUCT_ID);
        createdProductDto.setName(PRODUCT_NAME);
        createdProductDto.setPrice(PRODUCT_PRICE);
        createdProductDto.setQuantity(PRODUCT_QUANTITY);
        createdProductDto.setCategoryId(PRODUCT_CATEGORY_ID);
        createdProductDto.setVendorId(VENDOR_ID);

        when(productService.createProduct(any(ProductDto.class))).thenReturn(createdProductDto);

        mockMvc.perform(post("/products/createProduct")
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
     void createProduct_missingOrEmptyProductName() throws Exception {
        ProductDto productDto = createValidProductDto();
        productDto.setName(null);
        when(productService.createProduct(any(ProductDto.class))).thenThrow(InvalidArgumentException.class);

        mockMvc.perform(post("/products/createProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(productDto)))
                .andExpect(status().isBadRequest());

        verify(productService, times(1)).createProduct(any(ProductDto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create Product - Vendor Not Found")
     void createProduct_vendorNotFound() throws Exception {
        ProductDto productDto = createValidProductDto();
        productDto.setVendorId(3L);

        when(productService.createProduct(any(ProductDto.class))).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(post("/products/createProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(productDto)))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).createProduct(any(ProductDto.class));
    }

    @Test
    void getProduct() throws Exception {
        ProductDto productDto = createValidProductDto();
        productDto.setId(PRODUCT_ID);
        when(productService.getProductById(PRODUCT_ID)).thenReturn(productDto);

        mockMvc.perform(get("/products/{id}", PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productDto.getId()))
                .andExpect(jsonPath("$.name").value(productDto.getName()))
                .andExpect(jsonPath("$.price").value(productDto.getPrice()))
                .andExpect(jsonPath("$.quantity").value(productDto.getQuantity()))
                .andExpect(jsonPath("$.categoryId").value(productDto.getCategoryId()))
                .andExpect(jsonPath("$.categoryId").value(productDto.getVendorId()))
                .andDo(print());
    }

    @Test
    void getAllProducts() throws Exception {
        ProductDto productDto1 = createValidProductDto();
        productDto1.setId(PRODUCT_ID);
        ProductDto productDto2 = createValidProductDto();
        productDto2.setId(2L);
        productDto2.setName("Test Product2");

        List<ProductDto> productDtoList = List.of(productDto1, productDto2);
        when(productService.getAllProducts()).thenReturn(productDtoList);

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Product")))
                .andExpect(jsonPath("$[0].price", is(10.99)))
                .andExpect(jsonPath("$[0].quantity", is(5)))
                .andExpect(jsonPath("$[0].categoryId", is(1)))
                .andExpect(jsonPath("$[0].vendorId", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Test Product2")))
                .andExpect(jsonPath("$[1].price", is(10.99)))
                .andExpect(jsonPath("$[1].quantity", is(5)))
                .andExpect(jsonPath("$[1].categoryId", is(1)))
                .andExpect(jsonPath("$[1].vendorId", is(1)));

        verify(productService, times(1)).getAllProducts();


    }

    @Test
    void updateProduct_success() throws Exception {
        final ProductDto productDto = createValidProductDto();
        productDto.setId(PRODUCT_ID);
        productDto.setName("Updated Name");
        productDto.setPrice(new BigDecimal("60.99"));

        when(productService.updateProduct(eq(PRODUCT_ID), any(ProductDto.class))).thenReturn(productDto);

        mockMvc.perform(patch("/products/{id}", PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productDto.getId()))
                .andExpect(jsonPath("$.name").value(productDto.getName()))
                .andExpect(jsonPath("$.price").value(productDto.getPrice()))
                .andExpect(jsonPath("$.quantity").value(productDto.getQuantity()))
                .andExpect(jsonPath("$.categoryId").value(productDto.getCategoryId()))
                .andExpect(jsonPath("$.vendorId").value(productDto.getVendorId()));
        verify(productService, times(1)).updateProduct(PRODUCT_ID, productDto);
    }

    @Test
    void deleteProduct_success() throws Exception {
        mockMvc.perform(delete("/products/{id}", PRODUCT_ID))
                .andExpect(status().isOk())
                .andDo(print());
        verify(productService, times(1)).deleteProduct(PRODUCT_ID);
    }

    @Test
    void deleteProduct_productNotFound() throws Exception {
        final Long productId = 3L;
        doThrow(EntityNotFoundException.class).when(productService).deleteProduct(productId);
        mockMvc.perform(delete("/products/{id}", productId))
                .andExpect(status().isNotFound())
                .andDo(print());
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