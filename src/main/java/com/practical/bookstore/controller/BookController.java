package com.practical.bookstore.controller;

import com.practical.bookstore.dto.BookDto;
import com.practical.bookstore.dto.BookViewDto;
import com.practical.bookstore.dto.PageDto;
import com.practical.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<PageDto> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.getAllBooks(PageRequest.of(page, size)));
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<BookViewDto>> getBookByIsbn(@PathVariable String query) {
        return ResponseEntity.ok(bookService.getBookByIsbn(query));
    }

    @PostMapping
    public void createBook(@RequestBody BookDto bookDto) {
        bookService.createBook(bookDto);
    }

    @PutMapping
    public void updateBook(@RequestBody BookDto bookDto) {
        bookService.updateBook(bookDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
    }
}
