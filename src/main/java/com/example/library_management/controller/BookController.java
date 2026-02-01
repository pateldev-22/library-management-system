package com.example.library_management.controller;
import com.example.library_management.dto.BookDTO;
import com.example.library_management.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Management", description = "APIs for managing books")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    @Operation(summary = "Create a new book")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        log.info("REST: Creating book: {}", bookDTO.getTitle());
        BookDTO created = bookService.create(bookDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all books with pagination and sorting")
    public ResponseEntity<Page<BookDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        log.info("REST: Fetching books - Page: {}, Size: {}", page, size);
        return ResponseEntity.ok(bookService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        log.info("REST: Fetching book with ID: {}", id);
        return ResponseEntity.ok(bookService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        log.info("REST: Updating book with ID: {}", id);
        return ResponseEntity.ok(bookService.update(id, bookDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.info("REST: Deleting book with ID: {}", id);
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/title")
    @Operation(summary = "Search books by title (JPQL)")
    public ResponseEntity<List<BookDTO>> searchByTitle(@RequestParam String title) {
        log.info("REST: Searching books by title: {}", title);
        return ResponseEntity.ok(bookService.searchByTitle(title));
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Find book by ISBN (Native SQL)")
    public ResponseEntity<BookDTO> findByIsbn(@PathVariable String isbn) {
        log.info("REST: Finding book by ISBN: {}", isbn);
        return ResponseEntity.ok(bookService.findByIsbn(isbn));
    }

    @GetMapping("/search")
    @Operation(summary = "Search books dynamically (JPA Criteria API)")
    public ResponseEntity<List<BookDTO>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year) {
        log.info("REST: Searching books - Title: {}, Year: {}", title, year);
        return ResponseEntity.ok(bookService.searchBooks(title, year));
    }

    @GetMapping("/library/{libraryId}")
    @Operation(summary = "Get all books in a library")
    public ResponseEntity<List<BookDTO>> getBooksByLibrary(@PathVariable Long libraryId) {
        log.info("REST: Fetching books for library: {}", libraryId);
        return ResponseEntity.ok(bookService.findBooksByLibrary(libraryId));
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get all books by an author")
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(@PathVariable Long authorId) {
        log.info("REST: Fetching books for author: {}", authorId);
        return ResponseEntity.ok(bookService.findBooksByAuthor(authorId));
    }

    @PostMapping("/{bookId}/authors/{authorId}")
    @Operation(summary = "Add an author to a book (Many-to-Many)")
    public ResponseEntity<BookDTO> addAuthorToBook(@PathVariable Long bookId, @PathVariable Long authorId) {
        log.info("REST: Adding author {} to book {}", authorId, bookId);
        return ResponseEntity.ok(bookService.addAuthorToBook(bookId, authorId));
    }
}
