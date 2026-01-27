package com.example.library_management.repos;
import com.example.library_management.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // JPQL Query
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:title%")
    List<Book> searchByTitle(@Param("title") String title);

    // Native SQL Query
    @Query(value = "SELECT * FROM books WHERE isbn = :isbn", nativeQuery = true)
    Book findByIsbnNative(@Param("isbn") String isbn);

    // Pagination
    @Query("SELECT b FROM Book b WHERE b.publicationYear >= :year")
    Page<Book> findByYear(@Param("year") Integer year, Pageable pageable);
}
