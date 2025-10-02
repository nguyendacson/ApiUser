package com.example.ApiUser.dto.response.callData.getSlug;

import lombok.Data;

import java.util.List;

@Data
public class MoviePageResponse {
    private boolean status;
    private String msg;
    private List<MovieSlug> items;
    private Pagination pagination;
}

