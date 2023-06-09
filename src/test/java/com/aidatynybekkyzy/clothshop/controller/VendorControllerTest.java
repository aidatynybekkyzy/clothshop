package com.aidatynybekkyzy.clothshop.controller;


import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.dto.VendorDto;
import com.aidatynybekkyzy.clothshop.exception.exceptionHandler.GlobalExceptionHandler;
import com.aidatynybekkyzy.clothshop.repository.VendorRepository;
import com.aidatynybekkyzy.clothshop.service.impl.VendorServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class VendorControllerTest {
    @Mock
    private VendorServiceImpl vendorService;

    private MockMvc mockMvc;
    @InjectMocks
    private VendorController vendorController;
    private final static long ID = 1L;
    private final static String VENDOR_NAME = "Test vendor name ";

    final VendorDto vendorDto = VendorDto.builder()
            .id(ID)
            .vendorName("Test vendor name")
            .build();
    List<ProductDto> products = Arrays.asList(
            new ProductDto(1L, "Product 1", new BigDecimal(150), 4),
            new ProductDto(2L, "Product 2", new BigDecimal(200), 5)
    );
    @Autowired
    private VendorRepository vendorRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vendorController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllVendors_ShouldReturnListOfVendors() throws Exception {
        final VendorDto vendor1 = VendorDto.builder()
                .id(ID)
                .vendorName("Test vendor name 1")
                .build();
        final VendorDto vendor2 = VendorDto.builder()
                .id(2L)
                .vendorName("Test vendor name 2")
                .build();
        final List<VendorDto> vendorDtos = Arrays.asList(vendor1, vendor2);
        when(vendorService.getAllVendors()).thenReturn(vendorDtos);
        mockMvc.perform(MockMvcRequestBuilders.get("/vendors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].vendorName").value("Test vendor name 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].vendorName").value("Test vendor name 2"))
                .andDo(print());
    }

    @Test
    void createVendor() throws Exception {

        when(vendorService.createVendor(any(VendorDto.class))).thenReturn(vendorDto);

        mockMvc.perform(post("/vendors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendorDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.vendorName", is("Test vendor name")));
        verify(vendorService, times(1)).createVendor(any(VendorDto.class));

    }

    @Test
    void updateVendor_success() throws Exception {
        final VendorDto updatedVendor = VendorDto.builder()
                .id(ID)
                .vendorName("Updated vendor name")
                .build();
        when(vendorService.updateVendor(eq(ID), any(VendorDto.class))).thenReturn(updatedVendor);

        mockMvc.perform(patch("/vendors/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendorDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedVendor.getId()))
                .andExpect(jsonPath("$.vendorName").value(updatedVendor.getVendorName()));
        verify(vendorService, times(1)).updateVendor(eq(ID), any(VendorDto.class));

    }

    @Test
    void getVendorById_success() throws Exception {

        when(vendorService.getVendorById(ID)).thenReturn(vendorDto);
        mockMvc.perform(get("/vendors/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vendorDto.getId()))
                .andExpect(jsonPath("$.vendorName").value(vendorDto.getVendorName()))
                .andDo(print());
    }

    @Test
    void deleteVendorById() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/vendors/{id}", id))
                .andExpect(status().isOk())
                .andDo(print());
        verify(vendorService, times(1)).deleteVendorById(id);
    }

    @Test
    void addProductToVendor_success() throws Exception {
        Long vendorId = 1L;
        ProductDto productDto = ProductDto.builder()
                .id(ID)
                .name("Product 1")
                .price(new BigDecimal(200))
                .quantity(4)
                .vendorId(vendorId)
                .build();
        VendorDto updatedVendor = VendorDto.builder()
                .id(vendorId)
                .vendorName("Updated vendor name")
                .build();

        when(vendorService.addProductToVendor(eq(vendorId),any(ProductDto.class))).thenReturn(updatedVendor);

        mockMvc.perform(post("/vendors/{id}/products", vendorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedVendor.getId()))
                .andExpect(jsonPath("$.vendorName").value(updatedVendor.getVendorName()));
    }

    @Test
    void getVendorProducts_success() throws Exception {
        Long vendorId = 1L;
        when(vendorService.getVendorProducts(vendorId)).thenReturn(products);

        mockMvc.perform(get("/vendors/{vendorId}/products", vendorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(products.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[0].price").value(new BigDecimal(150)))
                .andExpect(jsonPath("$[0].quantity").value(4))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Product 2"))
                .andExpect(jsonPath("$[1].price").value(new BigDecimal(200)))
                .andExpect(jsonPath("$[1].quantity").value(5))
                .andDo(print());
        verify(vendorService, times(1)).getVendorProducts(vendorId);

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