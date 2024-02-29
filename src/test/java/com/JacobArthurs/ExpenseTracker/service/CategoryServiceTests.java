package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.CategoryRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.CategorySearchRequestDto;
import com.JacobArthurs.ExpenseTracker.dto.OperationResult;
import com.JacobArthurs.ExpenseTracker.dto.PaginatedResponse;
import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.model.User;
import com.JacobArthurs.ExpenseTracker.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CategoryService categoryService;

    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setTitle("Test Category");
        testCategory.setCreatedBy(testUser);
    }

    @Test
    void getAllCategories_ReturnsSortedCategories() {
        when(categoryRepository.findAllByCreatedBy(eq(testUser), any(Sort.class)))
                .thenReturn(Collections.singletonList(testCategory));
        when(currentUserProvider.getCurrentUser()).thenReturn(testUser);

        var categories = categoryService.getAllCategories();

        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());
        assertEquals(testCategory.getTitle(), categories.get(0).getTitle());
    }

    @Test
    void getCategoryById_FoundAndOwned_ReturnsCategory() {
        when(categoryRepository.findById(testCategory.getId())).thenReturn(Optional.of(testCategory));
        when(currentUserProvider.getCurrentUser()).thenReturn(testUser);

        var category = categoryService.getCategoryById(testCategory.getId());

        assertNotNull(category);
        assertEquals(testCategory.getTitle(), category.getTitle());
    }

    @Test
    void getCategoryById_NotFound_ThrowsRuntimeException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class, () -> categoryService.getCategoryById(99L));

        assertTrue(exception.getMessage().contains("Category not found with ID:"));
    }

    @Test
    void createCategory_Success_ReturnsOperationResult() {
        when(categoryRepository.countByCreatedBy(testUser)).thenReturn(9L);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);
        when(currentUserProvider.getCurrentUser()).thenReturn(testUser);

        var requestDto = new CategoryRequestDto();
        requestDto.setTitle("New Category");
        requestDto.setDescription("New Description");

        var result = categoryService.createCategory(requestDto);

        assertTrue(result.isSuccess());
        assertEquals("Category created successfully", result.getMessage());
    }

    @Test
    void updateCategory_Success_ReturnsOperationResult() {
        when(categoryRepository.findById(testCategory.getId())).thenReturn(Optional.of(testCategory));
        when(currentUserProvider.getCurrentUser()).thenReturn(testUser);

        var requestDto = new CategoryRequestDto();
        requestDto.setTitle("Updated Category");
        requestDto.setDescription("Updated Description");

        var result = categoryService.updateCategory(testCategory.getId(), requestDto);

        assertTrue(result.isSuccess());
        assertEquals("Category updated successfully", result.getMessage());
    }

    @Test
    void deleteCategory_Success_ReturnsOperationResult() {
        when(categoryRepository.findById(testCategory.getId())).thenReturn(Optional.of(testCategory));
        when(currentUserProvider.getCurrentUser()).thenReturn(testUser);

        var result = categoryService.deleteCategory(testCategory.getId());

        assertTrue(result.isSuccess());
        assertEquals("Category deleted successfully", result.getMessage());
    }

    @Test
    void searchCategories_ReturnsPaginatedCategories() {
        var requestDto = new CategorySearchRequestDto();
        requestDto.setTitle("Test");

        var page = new PageImpl<>(Collections.singletonList(testCategory));
        when(categoryRepository.findAll(any(Specification.class), any())).thenReturn(page);

        var response = categoryService.searchCategories(requestDto);

        assertFalse(response.getData().isEmpty());
        assertEquals(1, response.getTotal());
    }
}
