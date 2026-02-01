package com.example.library_management.repos;
import com.example.library_management.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // JPQL
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> searchByTitle(@Param("title") String title);

    // Native SQL
    @Query(value = "SELECT * FROM books WHERE isbn = :isbn", nativeQuery = true)
    Optional<Book> findByIsbnNative(@Param("isbn") String isbn);

    // JPQL + Pagination
    @Query("SELECT b FROM Book b WHERE b.publicationYear >= :year")
    Page<Book> findByYear(@Param("year") Integer year, Pageable pageable);

    // JPQL
    @Query("SELECT b FROM Book b WHERE b.library.ID = :libraryId")
    List<Book> findByLibraryId(@Param("libraryId") Long libraryId);

    // Native SQL — join through the junction table
    @Query(value = "SELECT b.* FROM books b JOIN book_author ba ON b.id = ba.book_id WHERE ba.author_id = :authorId",
            nativeQuery = true)
    List<Book> findByAuthorIdNative(@Param("authorId") Long authorId);

    // JPQL
    @Query("SELECT COUNT(b) FROM Book b WHERE b.library.ID = :libraryId")
    Long countByLibraryId(@Param("libraryId") Long libraryId);
}
