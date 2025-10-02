package com.example.ApiUser.dto.response.callData.getSlug;

import lombok.Data;

@Data
public class Pagination {
    private int totalItems;
    private int totalItemsPerPage;
    private int currentPage;
    private int totalPages;
}
