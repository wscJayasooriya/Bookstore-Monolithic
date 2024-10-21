package com.practical.bookstore.dto;

public record PageDto(
        int totalPages,
        long totalElements,
        Object content
) {
}
