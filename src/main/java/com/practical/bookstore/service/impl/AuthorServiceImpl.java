package com.practical.bookstore.service.impl;

import com.practical.bookstore.dto.AuthorDto;
import com.practical.bookstore.dto.AuthorViewDto;
import com.practical.bookstore.dto.PageDto;
import com.practical.bookstore.model.Author;
import com.practical.bookstore.repository.AuthorRepository;
import com.practical.bookstore.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public PageDto getAllAuthors(Pageable pageable) {
        log.info("request: AuthorController : getAllAuthors | Fetching all authors with pagination: {}", pageable);
        Page<Author> page = authorRepository.findAll(pageable);
        return new PageDto(
                page.getTotalPages(),
                page.getTotalElements(),
                page.getContent()
                        .stream()
                        .map(this::toAuthorViewDto)
                        .toList()
        );
    }

    @Override
    public List<AuthorViewDto> getAuthorByName(String name) {
        log.info("request: AuthorController : getAuthorByName | Fetching authors by name: {}", name);
        return authorRepository.findByFirstNameOrLastName(name)
                .stream()
                .map(this::toAuthorViewDto)
                .toList();
    }

    @Override
    public void createAuthor(AuthorDto authorDto) {
        log.info("request: AuthorController : createAuthor | authorDto : {} ", authorDto);
        validateAuthorRequest(authorDto);
        authorRepository.save(
                new Author(
                        authorDto.firstName(),
                        authorDto.lastName(),
                        authorDto.email()
                )
        );
        log.info("response: Author created successfully : {}", authorDto);
    }

    @Override
    public void updateAuthor(AuthorDto authorDto) {
        log.info("request: AuthorController : updateAuthor | Updating author with id: {} ", authorDto.id());
        validateAuthorRequest(authorDto);
        authorRepository.findById(authorDto.id())
                .map(author -> {
                    author.setFirstName(authorDto.firstName());
                    author.setLastName(authorDto.lastName());
                    author.setEmail(authorDto.email());
                    return authorRepository.save(author);
                })
                .orElseThrow(() -> {
                    log.error("Author not found with id: {}", authorDto.id());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author not found");
                });
        log.info("response: Author updated successfully: {}", authorDto);

    }

    @Override
    public void deleteAuthor(String id) {
        log.info("request: AuthorController : deleteAuthor | id : {} ", id);
        authorRepository.deleteById(id);
        log.info("response: Author deleted  successfully: {}", id);
    }

    private AuthorViewDto toAuthorViewDto(Author author) {
        return new AuthorViewDto(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                author.getEmail()
        );
    }

    private void validateAuthorRequest(AuthorDto authorDto) {
        if (authorDto.firstName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author name is required");
        if (authorDto.lastName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author name is required");
        if (authorDto.email().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author email is required");
    }
}
