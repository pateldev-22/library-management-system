package com.example.library_management.repos;
import com.example.library_management.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // JPQL
    @Query("SELECT a FROM Author a WHERE LOWER(a.name) = LOWER(:name)")
    Optional<Author> findByName(@Param("name") String name);

    // JPQL
    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Author> searchByName(@Param("name") String name);
}