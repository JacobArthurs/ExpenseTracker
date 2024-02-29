package com.JacobArthurs.ExpenseTracker.util;

import com.JacobArthurs.ExpenseTracker.dto.CategoryDto;
import com.JacobArthurs.ExpenseTracker.dto.PaginatedResponse;
import com.JacobArthurs.ExpenseTracker.model.Category;
import com.JacobArthurs.ExpenseTracker.model.ExpectedCategoryDistribution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PaginationUtilTests {
    @Test
    public void convertPaginatedToPaginatedDto_ConvertsSuccessfully() {
        var distribution1 = new ExpectedCategoryDistribution();
        distribution1.setId(1L);
        var distribution2 = new ExpectedCategoryDistribution();
        distribution2.setId(2L);

        var category1 = new Category();
        category1.setTitle("Category 1");
        category1.setExpectedCategoryDistribution(distribution1);

        var category2 = new Category();
        category2.setTitle("Category 2");
        category2.setExpectedCategoryDistribution(distribution2);

        var originalPaginatedResponse = new PaginatedResponse<>(2, 0, 2, Arrays.asList(category1, category2));

        var convertedPaginatedResponse = PaginationUtil.convertPaginatedToPaginatedDto(originalPaginatedResponse, CategoryDto::new);

        assertEquals(2, convertedPaginatedResponse.getLimit());
        assertEquals(0, convertedPaginatedResponse.getOffset());
        assertEquals(2, convertedPaginatedResponse.getTotal());
        assertEquals(2, convertedPaginatedResponse.getData().size());
        assertEquals("Category 1", convertedPaginatedResponse.getData().get(0).getTitle());
        assertEquals(1L, convertedPaginatedResponse.getData().get(0).getExpectedCategoryDistributionId());
        assertEquals("Category 2", convertedPaginatedResponse.getData().get(1).getTitle());
        assertEquals(2L, convertedPaginatedResponse.getData().get(1).getExpectedCategoryDistributionId());
    }
}
