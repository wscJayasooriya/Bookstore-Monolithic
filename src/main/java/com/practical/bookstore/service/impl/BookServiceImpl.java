package com.practical.bookstore.service.impl;

import com.practical.bookstore.dto.AuthorViewDto;
import com.practical.bookstore.dto.BookDto;
import com.practical.bookstore.dto.BookViewDto;
import com.practical.bookstore.dto.PageDto;
import com.practical.bookstore.model.Author;
import com.practical.bookstore.model.Book;
import com.practical.bookstore.repository.AuthorRepository;
import com.practical.bookstore.repository.BookRepository;
import com.practical.bookstore.service.BookService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;


    @Override
    public void createBook(BookDto bookDto) {
        log.info("request: BookController : createBook | bookDto : {} ", bookDto);
        validateBookRequest(bookDto);
        List<Author> authors = authorRepository.findByIdIn(bookDto.authors());
        bookRepository.save(
                new Book(
                        bookDto.isbn(),
                        bookDto.title(),
                        bookDto.publishedDate(),
                        bookDto.price(),
                        authors
                )
        );
        log.info("response: Book created successfully : {}", bookDto);
    }


    @Override
    public void deleteBook(String id) {
        log.info("request: BookController : deleteBook | id : {} ", id);
        bookRepository.deleteById(id);
        log.info("response: Book deleted  successfully: {}", id);
    }

    @Override
    public List<BookViewDto> getBookByIsbn(String query) {
        log.info("request: BookController : getBookByIsbn | Fetching books by query: {}", query);
        return bookRepository.findByIsbnOrTitle(query)
                .stream()
                .map(this::toBookViewDto)
                .toList();
    }

    @Transactional
    @Override
    public PageDto getAllBooks(Pageable pageable) {
        log.info("request: BookController : getAllBooks | Fetching all books with pagination: {}", pageable);
        Page<Book> page = bookRepository.findAll(pageable);
        return new PageDto(
                page.getTotalPages(),
                page.getTotalElements(),
                page.getContent()
                        .stream()
                        .map(this::toBookViewDto)
                        .toList()
        );
    }

    @Override
    public void updateBook(BookDto bookDto) {
        log.info("request: BookController : updateBook | Updating book with id: {} ", bookDto.id());
        validateUpdateBookRequest(bookDto);
        List<Author> authors = authorRepository.findByIdIn(bookDto.authors());
        bookRepository.findById(bookDto.id())
                .ifPresentOrElse(
                        book -> {
                            book.setIsbn(bookDto.isbn());
                            book.setTitle(bookDto.title());
                            book.setPublishedDate(bookDto.publishedDate());
                            book.setPrice(bookDto.price());
                            book.setAuthors(Set.copyOf(authors));
                            bookRepository.save(book);
                        },
                        () -> {
                            log.error("Book not found with id: {}", bookDto.id());
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found");
                        }
                );
        log.info("response: Book updated successfully: {}", bookDto);
    }


    private BookViewDto toBookViewDto(Book book) {
        return new BookViewDto(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getPublishedDate(),
                book.getPrice(),
                book.getAuthors()
                        .stream()
                        .map(this::toAuthorViewDto)
                        .toList()
        );
    }

    private void validateUpdateBookRequest(BookDto bookDto) {
        if (bookDto.id() == null || bookDto.id().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "uuid is required");

        validateBookRequest(bookDto);
    }

    private void validateBookRequest(BookDto bookDto) {
        if (bookDto.isbn() == null || bookDto.isbn().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "isbn is required");

        if (bookDto.title() == null || bookDto.title().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required");

        if (bookDto.authors() == null || bookDto.authors().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authors are required");

        if (bookRepository.existsByIsbn(bookDto.isbn())) {
            log.error("Book with ISBN {} already exists", bookDto.isbn());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ISBN must be unique");
        }
    }

    private AuthorViewDto toAuthorViewDto(Author author) {
        return new AuthorViewDto(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                author.getEmail()
        );
    }

}
