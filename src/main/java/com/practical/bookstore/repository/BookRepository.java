package com.practical.bookstore.repository;

import com.practical.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    @Query("""
            select b from Book b
            where b.isbn like %?1% or
            b.title like %?1%
            """)
    List<Book> findByIsbnOrTitle(String query);

    boolean existsByIsbn(String isbn);
}
