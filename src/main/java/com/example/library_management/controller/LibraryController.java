package com.example.library_management.controller;
import com.example.library_management.dto.LibraryDTO;
import com.example.library_management.services.LibraryService;
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

@RestController
@RequestMapping("/api/libraries")
@Tag(name = "Library Management", description = "APIs for managing libraries")
@Slf4j
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @PostMapping
    @Operation(summary = "Create a new library")
    public ResponseEntity<LibraryDTO> createLibrary(@Valid @RequestBody LibraryDTO libraryDTO) {
        log.info("REST: Creating library: {}", libraryDTO.getName());
        LibraryDTO created = libraryService.create(libraryDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all libraries with pagination and sorting")
    public ResponseEntity<Page<LibraryDTO>> getAllLibraries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        log.info("REST: Fetching libraries - Page: {}, Size: {}", page, size);
        return ResponseEntity.ok(libraryService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get library by ID")
    public ResponseEntity<LibraryDTO> getLibraryById(@PathVariable Long id) {
        log.info("REST: Fetching library with ID: {}", id);
        return ResponseEntity.ok(libraryService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update library")
    public ResponseEntity<LibraryDTO> updateLibrary(@PathVariable Long id, @Valid @RequestBody LibraryDTO libraryDTO) {
        log.info("REST: Updating library with ID: {}", id);
        return ResponseEntity.ok(libraryService.update(id, libraryDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete library")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
        log.info("REST: Deleting library with ID: {}", id);
        libraryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Find library by name")
    public ResponseEntity<LibraryDTO> findByName(@PathVariable String name) {
        log.info("REST: Finding library by name: {}", name);
        return ResponseEntity.ok(libraryService.findByName(name));
    }

    @GetMapping("/{id}/book-count")
    @Operation(summary = "Count books in a library")
    public ResponseEntity<Long> countBooks(@PathVariable Long id) {
        log.info("REST: Counting books in library: {}", id);
        return ResponseEntity.ok(libraryService.countBooksInLibrary(id));
    }
}
