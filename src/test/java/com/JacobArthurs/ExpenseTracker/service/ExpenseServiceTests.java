package com.JacobArthurs.ExpenseTracker.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import com.JacobArthurs.ExpenseTracker.dto.*;
import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.model.Expense;
import com.JacobArthurs.ExpenseTracker.model.User;
import com.JacobArthurs.ExpenseTracker.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTests {
    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense testExpense;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        var testUser = new User();
        testUser.setId(1L);

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setTitle("Test Category");

        testExpense = new Expense();
        testExpense.setId(1L);
        testExpense.setAmount(BigDecimal.valueOf(20.50));
        testExpense.setCategory(testCategory);
        testExpense.setCreatedBy(testUser);
        testExpense.setCreatedDate(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void getAllExpenses_ReturnsAllExpenses() {
        when(expenseRepository.findAll()).thenReturn(Arrays.asList(testExpense, testExpense));

        var expenses = expenseService.getAllExpenses();

        assertEquals(2, expenses.size());
        verify(expenseRepository).findAll();
    }

    @Test
    void getExpenseById_WhenAuthorized_ReturnsExpense() {
        when(expenseRepository.findById(anyLong())).thenReturn(Optional.of(testExpense));
        when(currentUserProvider.getCurrentUser()).thenReturn(testExpense.getCreatedBy());

        var expense = expenseService.getExpenseById(1L);

        assertEquals(testExpense, expense);
        verify(expenseRepository).findById(1L);
    }

    @Test
    void getExpenseById_WhenNotAuthorized_ThrowsException() {
        var anotherUser = new User();
        anotherUser.setId(2L);

        when(expenseRepository.findById(anyLong())).thenReturn(Optional.of(testExpense));
        when(currentUserProvider.getCurrentUser()).thenReturn(anotherUser);

        var exception = assertThrows(RuntimeException.class, () -> {
            expenseService.getExpenseById(1L);
        });

        var expectedMessage = "You are not authorized to get an expense that is not yours.";
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createExpense_SavesExpense_ReturnsSuccess() {
        var requestDto = new ExpenseRequestDto();
        requestDto.setCategoryId(testCategory.getId());
        requestDto.setTitle("Test Expense");
        requestDto.setAmount(new BigDecimal("100.00"));

        when(categoryService.getCategoryById(testCategory.getId())).thenReturn(testCategory);
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);

        var result = expenseService.createExpense(requestDto);

        assertTrue(result.isSuccess());
        assertEquals("Expense created successfully", result.getMessage());
        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    void updateExpense_WhenAuthorized_UpdatesExpense() {
        var requestDto = new ExpenseRequestDto();
        requestDto.setCategoryId(testCategory.getId());
        requestDto.setTitle("Updated Expense");
        requestDto.setAmount(new BigDecimal("150.00"));

        when(expenseRepository.findById(testExpense.getId())).thenReturn(Optional.of(testExpense));
        when(categoryService.getCategoryById(testCategory.getId())).thenReturn(testCategory);
        when(currentUserProvider.getCurrentUser()).thenReturn(testExpense.getCreatedBy());

        var result = expenseService.updateExpense(testExpense.getId(), requestDto);

        assertTrue(result.isSuccess());
        assertEquals("Expense updated successfully", result.getMessage());
        verify(expenseRepository).save(testExpense);
    }

    @Test
    void deleteExpense_WhenAuthorized_DeletesExpense() {
        when(expenseRepository.findById(testExpense.getId())).thenReturn(Optional.of(testExpense));
        when(currentUserProvider.getCurrentUser()).thenReturn(testExpense.getCreatedBy());

        var result = expenseService.deleteExpense(testExpense.getId());

        assertTrue(result.isSuccess());
        assertEquals("Expense deleted successfully", result.getMessage());
        verify(expenseRepository).deleteById(testExpense.getId());
    }

    @Test
    void searchExpenses_ReturnsMatchingExpenses() {
        var requestDto = new ExpenseSearchRequestDto();
        requestDto.setCategoryId(testCategory.getId());
        requestDto.setTitle("Test");

        var expensePage = new PageImpl<>(List.of(testExpense));

        when(expenseRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expensePage);

        var response = expenseService.searchExpenses(requestDto);

        assertEquals(1, response.getTotal());
        assertEquals(testExpense, response.getData().get(0));
        verify(expenseRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getCurrentDistribution_ReturnsDistribution() {
        var requestDto = new CurrentDistributionRequestDto();
        requestDto.setMonthCount(6);

        when(expenseRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(testExpense));
        when(categoryService.getAllCategories()).thenReturn(List.of(testCategory));

        var distribution = expenseService.getCurrentDistribution(requestDto);

        assertFalse(distribution.isEmpty());
        assertEquals(1, distribution.size());

        var dto = distribution.get(0);
        assertEquals(testCategory.getId(), dto.getCategoryId());
        assertEquals(100, dto.getDistribution());
    }

    @Test
    void getMonthlyExpenseMetric_ReturnsMetrics() {
        when(expenseRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(testExpense));

        var metricDto = expenseService.getMonthlyExpenseMetric();

        assertNotNull(metricDto);
        assertFalse(metricDto.getMonths().isEmpty());
        assertFalse(metricDto.getAmounts().isEmpty());

        assertTrue(metricDto.getAmounts().stream().anyMatch(amount -> amount.compareTo(BigDecimal.ZERO) > 0));
    }

    @Test
    void getTotalExpenseAmount_ReturnsTotalForMonth() {
        var requestDto = new TotalExpenseAmountRequestDto();
        requestDto.setMonth(Timestamp.valueOf(LocalDate.now().atStartOfDay()));

        when(expenseRepository.findAll(any(Specification.class), eq(Sort.unsorted()))).thenReturn(List.of(testExpense));

        var totalAmount = expenseService.getTotalExpenseAmount(requestDto);

        assertNotNull(totalAmount);
        assertTrue(totalAmount.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void reassignExpensesToNewCategory_UpdatesExpenses() {
        var newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setTitle("New Category");

        var requestDto = new ReassignExpenseCategoryRequestDto();
        requestDto.setOldCategoryId(testCategory.getId());
        requestDto.setNewCategoryId(newCategory.getId());

        when(expenseRepository.findAll(any(Specification.class), eq(Sort.unsorted()))).thenReturn(List.of(testExpense));
        when(categoryService.getCategoryById(newCategory.getId())).thenReturn(newCategory);

        var result = expenseService.reassignExpensesToNewCategory(requestDto);

        assertTrue(result.isSuccess());
        assertEquals("1 expenses successfully updated.", result.getMessage());
        verify(expenseRepository, times(1)).save(testExpense);
    }
}
