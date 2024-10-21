package com.practical.bookstore.service;

import com.practical.bookstore.dto.BookDto;
import com.practical.bookstore.dto.BookViewDto;
import com.practical.bookstore.dto.PageDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {

    void createBook(BookDto bookDto);

    void deleteBook(String id);

    List<BookViewDto> getBookByIsbn(String query);

    PageDto getAllBooks(Pageable pageable);


    void updateBook(BookDto bookDto);
}
