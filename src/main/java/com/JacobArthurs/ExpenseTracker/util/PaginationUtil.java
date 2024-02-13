package com.JacobArthurs.ExpenseTracker.util;

import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.List;

import com.JacobArthurs.ExpenseTracker.dto.PaginatedResponse;

public class PaginationUtil {
    /**
    * Converts a PaginatedResponse of one type to a PaginatedResponse of another type using a converter function.
    *
    * @param <T> the type of elements in the original PaginatedResponse
    * @param <R> the type of elements in the converted PaginatedResponse
    * @param paginatedResponse the original PaginatedResponse to be converted
    * @param converter         the function to convert elements from type T to type R
    * @return a new PaginatedResponse with elements of type R
    */
    public static <T, R> PaginatedResponse<R> convertPaginatedToPaginatedDto(PaginatedResponse<T> paginatedResponse, Function<T, R> converter) {
        List<R> dtos = paginatedResponse.getData().stream()
                .map(converter)
                .collect(Collectors.toList());
        return new PaginatedResponse<>(paginatedResponse.getLimit(), paginatedResponse.getOffset(), paginatedResponse.getTotal(), dtos);
    }
}
