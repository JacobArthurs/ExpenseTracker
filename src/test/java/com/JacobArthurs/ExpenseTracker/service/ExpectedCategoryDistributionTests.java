package com.JacobArthurs.ExpenseTracker.service;

import com.JacobArthurs.ExpenseTracker.dto.*;
import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.model.ExpectedCategoryDistribution;
import com.JacobArthurs.ExpenseTracker.model.User;
import com.JacobArthurs.ExpenseTracker.repository.ExpectedCategoryDistributionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpectedCategoryDistributionTests {
    @Mock
    private ExpectedCategoryDistributionRepository expectedCategoryDistributionRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private ExpectedCategoryDistributionService expectedCategoryDistributionService;

    private User testUser;
    private Category testCategory;
    private ExpectedCategoryDistribution testDistribution;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setTitle("Test Category");

        testDistribution = new ExpectedCategoryDistribution();
        testDistribution.setId(1L);
        testDistribution.setCategory(testCategory);
        testDistribution.setDistribution(20);
        testDistribution.setCreatedBy(testUser);
    }

    @Test
    void getAllExpectedCategoryDistributions_ReturnsAllDistributions() {
        when(expectedCategoryDistributionRepository.findAll()).thenReturn(Collections.singletonList(testDistribution));

        var distributions = expectedCategoryDistributionService.getAllExpectedCategoryDistributions();

        assertFalse(distributions.isEmpty());
        assertEquals(1, distributions.size());
        assertEquals(testDistribution.getId(), distributions.get(0).getId());
    }

    @Test
    void getAllDistributions_ReturnsDistributionsForCurrentUser() {
        when(expectedCategoryDistributionRepository.findAllByCreatedBy(eq(testUser), any(Sort.class)))
                .thenReturn(Collections.singletonList(testDistribution));
        when(currentUserProvider.getCurrentUser()).thenReturn(testUser);

        var distributionDtos = expectedCategoryDistributionService.getAllDistributions();

        assertFalse(distributionDtos.isEmpty());
        assertEquals(1, distributionDtos.size());
        assertEquals(testDistribution.getId(), distributionDtos.get(0).getId());
    }

    @Test
    void getExpectedCategoryDistributionById_FoundAndOwned_ReturnsDistribution() {
        when(expectedCategoryDistributionRepository.findById(testDistribution.getId())).thenReturn(Optional.of(testDistribution));
        when(currentUserProvider.getCurrentUser()).thenReturn(testUser);

        var distribution = expectedCategoryDistributionService.getExpectedCategoryDistributionById(testDistribution.getId());

        assertNotNull(distribution);
        assertEquals(testDistribution.getId(), distribution.getId());
    }

    @Test
    void createExpectedCategoryDistribution_Success_ReturnsOperationResult() {
        when(categoryService.getCategoryById(testCategory.getId())).thenReturn(testCategory);
        when(expectedCategoryDistributionRepository.save(any(ExpectedCategoryDistribution.class))).thenReturn(testDistribution);

        var requestDto = new ExpectedCategoryDistributionRequestDto(testCategory.getId(), 20);

        var result = expectedCategoryDistributionService.createExpectedCategoryDistribution(requestDto);

        assertTrue(result.isSuccess());
        assertEquals("Expected category distribution created successfully", result.getMessage());
    }

    @Test
    void updateExpectedCategoryDistribution_Success_ReturnsOperationResult() {
        when(expectedCategoryDistributionRepository.findById(testDistribution.getId())).thenReturn(Optional.of(testDistribution));
        when(currentUserProvider.getCurrentUser()).thenReturn(testUser);

        var requestDto = new ExpectedCategoryDistributionRequestDto(testCategory.getId(), 25);

        var result = expectedCategoryDistributionService.updateExpectedCategoryDistribution(testDistribution.getId(), requestDto);

        assertTrue(result.isSuccess());
        assertEquals("Expected category distribution updated successfully", result.getMessage());
    }

    @Test
    void deleteExpectedCategoryDistribution_Success_ReturnsOperationResult() {
        when(expectedCategoryDistributionRepository.findById(testDistribution.getId())).thenReturn(Optional.of(testDistribution));
        when(currentUserProvider.getCurrentUser()).thenReturn(testUser);

        var result = expectedCategoryDistributionService.deleteExpectedCategoryDistribution(testDistribution.getId());

        assertTrue(result.isSuccess());
        assertEquals("Expected category distribution deleted successfully", result.getMessage());
    }

    @Test
    void searchExpectedCategoryDistributions_ReturnsPaginatedDistributions() {
        var requestDto = new ExpectedCategoryDistributionSearchRequestDto();
        var page = new PageImpl<>(Collections.singletonList(testDistribution));
        when(expectedCategoryDistributionRepository.findAll(any(Specification.class), any())).thenReturn(page);

        var response = expectedCategoryDistributionService.searchExpectedCategoryDistributions(requestDto);

        assertFalse(response.getData().isEmpty());
        assertEquals(1, response.getTotal());
    }
}
