package com.JacobArthurs.ExpenseTracker.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PaginatedResponse<T> {
    private long limit;

    private long offset;

    private long total;

    private List<T> data;

    public PaginatedResponse(long limit, long offset, long total, List<T> data) {
        this.limit = limit;
        this.offset = offset;
        this.total = total;
        this.data = data;
    }
}
