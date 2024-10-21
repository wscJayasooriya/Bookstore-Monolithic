package com.practical.bookstore.dto;

public record AuthorDto(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
