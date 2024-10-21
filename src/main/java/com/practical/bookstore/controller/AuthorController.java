package com.practical.bookstore.controller;

import com.practical.bookstore.dto.AuthorDto;
import com.practical.bookstore.dto.AuthorViewDto;
import com.practical.bookstore.dto.PageDto;
import com.practical.bookstore.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/v1/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public ResponseEntity<PageDto> getAllAuthors(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(authorService.getAllAuthors(PageRequest.of(page, size)));
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<AuthorViewDto>> getAuthorByName(@PathVariable String name) {
        return ResponseEntity.ok(authorService.getAuthorByName(name));
    }

    @PostMapping
    public void createAuthor(@RequestBody AuthorDto authorDto) {
        authorService.createAuthor(authorDto);
    }

    @PutMapping
    public void updateAuthor(@RequestBody AuthorDto authorDto) {
        authorService.updateAuthor(authorDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable String id) {
        authorService.deleteAuthor(id);
    }

}
