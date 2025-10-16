package com.example.ApiUser.service.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginationHelper {
    public Pageable buildPageable(int page, int size, String sortDirection, String sortBy) {
        return PageRequest.of(page, size, buildSort(sortDirection, sortBy));
    }

    public Sort buildSort(String sortDirection, String sortBy) {
        Sort.Direction direction = sortDirection != null && sortDirection.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        String sortField = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;

        return Sort.by(direction, sortField);
    }
}
