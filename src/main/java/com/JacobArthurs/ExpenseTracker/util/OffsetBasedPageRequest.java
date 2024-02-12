package com.JacobArthurs.ExpenseTracker.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * A custom implementation of {@link Pageable} for offset-based pagination.
 */
public class OffsetBasedPageRequest implements Pageable {
    private final int offset;
    private final int limit;
    private Sort sort;

    /**
     * Constructs an instance of {@link OffsetBasedPageRequest} with the given offset, limit, and sort.
     *
     * @param offset the offset of the page to be returned, 0-based index
     * @param limit  the size of the page to be returned
     * @param sort   the sorting parameters for the page
     */
    public OffsetBasedPageRequest(int offset, int limit, Sort sort) {
        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    /**
     * Constructs an instance of {@link OffsetBasedPageRequest} with the given offset and limit.
     *
     * @param offset the offset of the page to be returned, 0-based index
     * @param limit  the size of the page to be returned
     */
    public OffsetBasedPageRequest(int offset, int limit) {
        this(offset, limit, Sort.unsorted());
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return this.sort;
    }

    @Override
    public Pageable next() {
        return new OffsetBasedPageRequest((int) (getOffset() + getPageSize()), getPageSize(), getSort());
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? new OffsetBasedPageRequest((int) Math.max(0, getOffset() - getPageSize()), getPageSize(), getSort()) : this;
    }

    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(0, getPageSize(), getSort());
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetBasedPageRequest(pageNumber * getPageSize(), getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return getOffset() > getPageSize();
    }
}
