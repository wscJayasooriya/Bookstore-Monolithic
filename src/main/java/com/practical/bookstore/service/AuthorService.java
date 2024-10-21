package com.practical.bookstore.service;

import com.practical.bookstore.dto.AuthorDto;
import com.practical.bookstore.dto.AuthorViewDto;
import com.practical.bookstore.dto.PageDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorService {

    PageDto getAllAuthors(Pageable pageable);

    List<AuthorViewDto> getAuthorByName(String name);

    void createAuthor(AuthorDto authorDto);

    void updateAuthor(AuthorDto authorDto);

    void deleteAuthor(String id);

}
