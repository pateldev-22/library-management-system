package com.example.library_management.controller;
import com.example.library_management.dto.AuthorDTO;
import com.example.library_management.services.AuthorService;
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
@RequestMapping("/api/authors")
@Tag(name = "Author Management", description = "APIs for managing authors")
@Slf4j
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping
    @Operation(summary = "Create a new author")
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        log.info("REST: Creating author: {}", authorDTO.getName());
        AuthorDTO created = authorService.create(authorDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all authors with pagination")
    public ResponseEntity<Page<AuthorDTO>> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        log.info("REST: Fetching all authors");
        return ResponseEntity.ok(authorService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        log.info("REST: Fetching author with ID: {}", id);
        return ResponseEntity.ok(authorService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update author")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDTO authorDTO) {
        log.info("REST: Updating author with ID: {}", id);
        return ResponseEntity.ok(authorService.update(id, authorDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete author")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        log.info("REST: Deleting author with ID: {}", id);
        authorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Find author by exact name")
    public ResponseEntity<AuthorDTO> findByName(@PathVariable String name) {
        log.info("REST: Finding author by name: {}", name);
        return ResponseEntity.ok(authorService.findByName(name));
    }

    @GetMapping("/search")
    @Operation(summary = "Search authors by name (partial match)")
    public ResponseEntity<List<AuthorDTO>> searchByName(@RequestParam String name) {
        log.info("REST: Searching authors by name: {}", name);
        return ResponseEntity.ok(authorService.searchByName(name));
    }
}
