package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.JsonUtils;
import com.aidatynybekkyzy.clothshop.dto.CategoryDto;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.exception.EntityAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.exception.EntityNotFoundException;
import com.aidatynybekkyzy.clothshop.exception.InvalidArgumentException;
import com.aidatynybekkyzy.clothshop.exception.exceptionhandler.GlobalExceptionHandler;
import com.aidatynybekkyzy.clothshop.service.common.ResponseErrorValidation;
import com.aidatynybekkyzy.clothshop.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
class CategoryControllerTest {
    @Mock
    private CategoryServiceImpl categoryService;
    private MockMvc mockMvc;
    @InjectMocks
    private CategoryController categoryController;
    @Mock
    ResponseErrorValidation responseErrorValidation;
    private final static long ID = 1L;
    private final static String CATEGORY_NAME = "Test category name ";

    final CategoryDto mockCategory = CategoryDto.builder()
            .id(ID)
            .categoryName(CATEGORY_NAME)
            .build();
    final CategoryDto category1 = CategoryDto.builder()
            .id(ID)
            .categoryName("Category 1").build();
    final CategoryDto category2 = CategoryDto.builder()
            .id(2L)
            .categoryName("Category 2").build();
    final List<CategoryDto> categories = Arrays.asList(category1, category2);

    List<ProductDto> products = Arrays.asList(
            new ProductDto(1L, "Product 1", new BigDecimal(150), 4),
            new ProductDto(2L, "Product 2", new BigDecimal(200), 5)
    );

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController, responseErrorValidation)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
     void testCreateCategory_success() throws Exception {
        final CategoryDto createdMockCategory = CategoryDto.builder()
                .id(ID)
                .categoryName(CATEGORY_NAME)
                .build();
        when(categoryService.createCategory(any(CategoryDto.class))).thenReturn(createdMockCategory);

        mockMvc.perform(post("/categories/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.asJsonString(createdMockCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.categoryName", is("Test category name ")));

        verify(categoryService, times(1)).createCategory(any(CategoryDto.class));
    }
    @Test
     void testCreateCategory_InvalidArgumentException() throws Exception {
        final CategoryDto invalidCategory = CategoryDto.builder()
                .categoryName("") // Empty category name
                .build();

        when(categoryService.createCategory(any(CategoryDto.class))).thenThrow(InvalidArgumentException.class);

        mockMvc.perform(post("/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(invalidCategory)))
                .andExpect(status().isBadRequest());

        verify(categoryService, times(1)).createCategory(any(CategoryDto.class));
    }
    @Test
    @DisplayName("Get Category by Id - Error: Category Already Exists")
     void testCreateCategory_CategoryAlreadyExistsException() throws Exception {
        final CategoryDto existingCategory = CategoryDto.builder()
                .categoryName("Existing Category")
                .build();

        when(categoryService.createCategory(any(CategoryDto.class))).thenThrow(EntityAlreadyExistsException.class);

        mockMvc.perform(post("/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(existingCategory)))
                .andExpect(status().isConflict());

        verify(categoryService, times(1)).createCategory(any(CategoryDto.class));
    }


    @Test
    @DisplayName("Get Category by Id ")
    void getCategory_success() throws Exception {
        when(categoryService.getCategoryById(ID)).thenReturn(mockCategory);
        mockMvc.perform(get("/categories/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockCategory.getId()))
                .andExpect(jsonPath("$.categoryName").value(mockCategory.getCategoryName()))
                .andDo(print());
    }
    @Test
    @DisplayName("Get Category by Id - Error: Category Not Found")
    void getCategory_categoryNotFound() throws Exception {
        final Long categoryId = 1L;

        when(categoryService.getCategoryById(categoryId)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/categories/{id}", categoryId))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).getCategoryById(categoryId);
    }

    @Test
    @DisplayName("Get All Categories")
     void testGetAllCategories_success() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].categoryName").value("Category 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].categoryName").value("Category 2"))
                .andDo(print());
    }

    @Test
    void updateCategory_success() throws Exception {
        final CategoryDto updatedCategoryDto = CategoryDto.builder()
                .id(ID)
                .categoryName("Updated Category name")
                .build();
        when(categoryService.updateCategory(eq(ID), any(CategoryDto.class))).thenReturn(updatedCategoryDto);

        mockMvc.perform(patch("/categories/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(mockCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedCategoryDto.getId()))
                .andExpect(jsonPath("$.categoryName").value(updatedCategoryDto.getCategoryName()));
    }
    @Test
    @DisplayName("Update Category - Error: Invalid Argument")
    void updateCategory_invalidArgument() throws Exception {
        final Long categoryId = 1L;
        final CategoryDto updatedCategoryDto = CategoryDto.builder()
                .id(categoryId)
                .categoryName(null)
                .build();

        when(categoryService.updateCategory(eq(categoryId), any(CategoryDto.class)))
                .thenThrow(InvalidArgumentException.class);

        mockMvc.perform(patch("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(updatedCategoryDto)))
                .andExpect(status().isBadRequest());

        verify(categoryService, times(1)).updateCategory(eq(categoryId), any(CategoryDto.class));
    }

    @Test
    @DisplayName("Delete Category by Id ")
    void deleteCategory_success() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/categories/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());
        verify(categoryService, times(1)).deleteCategory(id);
    }
    @Test
    @DisplayName("Delete Category - Error: Category Not Found")
    void deleteCategory_categoryNotFound() throws Exception {
        final Long categoryId = 10L;
        doThrow(EntityNotFoundException.class)
                .when(categoryService).deleteCategory(categoryId);

        mockMvc.perform(delete("/categories/{id}", categoryId))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).deleteCategory(categoryId);
    }

    @Test
    @DisplayName("Get All Products of a Category by CategoryId ")
    void getAllProductsByCategoryId_success() throws Exception {
        Long categoryId = 1L;
        when(categoryService.getAllProductsByCategoryId(categoryId)).thenReturn(products);
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/{categoryId}/products", categoryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
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


    }

}