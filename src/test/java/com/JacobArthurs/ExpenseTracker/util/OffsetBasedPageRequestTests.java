package com.JacobArthurs.ExpenseTracker.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OffsetBasedPageRequestTests {
    @Test
    void constructorWithOffsetAndLimitAndSort() {
        var sort = Sort.by(Sort.Order.desc("id"));
        var pageRequest = new OffsetBasedPageRequest(10, 5, sort);

        assertEquals(10, pageRequest.getOffset());
        assertEquals(5, pageRequest.getPageSize());
        assertEquals(sort, pageRequest.getSort());
    }

    @Test
    void constructorWithOffsetAndLimit() {
        var pageRequest = new OffsetBasedPageRequest(10, 5);

        assertEquals(10, pageRequest.getOffset());
        assertEquals(5, pageRequest.getPageSize());
        assertEquals(Sort.unsorted(), pageRequest.getSort());
    }

    @Test
    void getPageNumber() {
        var pageRequest = new OffsetBasedPageRequest(10, 5);
        assertEquals(2, pageRequest.getPageNumber());
    }

    @Test
    void next() {
        var pageRequest = new OffsetBasedPageRequest(10, 5);
        Pageable nextPage = pageRequest.next();

        assertEquals(15, nextPage.getOffset());
        assertEquals(5, nextPage.getPageSize());
    }

    @Test
    void previousOrFirst_WhenPreviousExists() {
        var pageRequest = new OffsetBasedPageRequest(10, 5);
        Pageable previousPage = pageRequest.previousOrFirst();

        assertEquals(5, previousPage.getOffset());
        assertEquals(5, previousPage.getPageSize());
    }

    @Test
    void previousOrFirst_WhenFirstPage() {
        var pageRequest = new OffsetBasedPageRequest(0, 5);
        var previousOrFirstPage = pageRequest.previousOrFirst();

        assertEquals(0, previousOrFirstPage.getOffset());
        assertEquals(5, previousOrFirstPage.getPageSize());
    }

    @Test
    void first() {
        var pageRequest = new OffsetBasedPageRequest(10, 5);
        var firstPage = pageRequest.first();

        assertEquals(0, firstPage.getOffset());
        assertEquals(5, firstPage.getPageSize());
    }

    @Test
    void withPage() {
        var pageRequest = new OffsetBasedPageRequest(10, 5);
        var specificPage = pageRequest.withPage(2);

        assertEquals(10, specificPage.getOffset());
        assertEquals(5, specificPage.getPageSize());
    }

    @Test
    void hasPrevious_WhenHasPrevious() {
        var pageRequest = new OffsetBasedPageRequest(10, 5);
        assertTrue(pageRequest.hasPrevious());
    }

    @Test
    void hasPrevious_WhenNoPrevious() {
        var pageRequest = new OffsetBasedPageRequest(0, 5);
        assertFalse(pageRequest.hasPrevious());
    }
}
