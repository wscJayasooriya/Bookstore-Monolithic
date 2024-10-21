package com.practical.bookstore.dto;

import java.time.LocalDate;
import java.util.List;

public record BookDto(
        String id,
        String isbn,
        String title,
        LocalDate publishedDate,
        Double price,
        List<String> authors
) {
}
