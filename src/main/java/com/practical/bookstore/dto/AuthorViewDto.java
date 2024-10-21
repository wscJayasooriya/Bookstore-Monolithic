package com.practical.bookstore.dto;

public record AuthorViewDto(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
