package com.practical.bookstore.repository;

import com.practical.bookstore.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

    List<Author> findByIdIn(List<String> ids);

    @Query("""
            select a from Author a
            where a.firstName like %?1% or
            a.lastName like %?1%
            """)
    List<Author> findByFirstNameOrLastName(String name);
}
