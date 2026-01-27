package com.example.library_management.controller;

import com.example.library_management.dto.LibraryDTO;
import com.example.library_management.services.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/libraries")
@Tag(name = "Library Management")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @PostMapping
    @Operation(summary = "Create a new library")
    public ResponseEntity<LibraryDTO> createLibrary(@Valid @RequestBody LibraryDTO libraryDTO) {
        LibraryDTO created = libraryService.create(libraryDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all libraries")
    public ResponseEntity<Page<LibraryDTO>> getAllLibraries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(libraryService.getAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get library by ID")
    public ResponseEntity<LibraryDTO> getLibraryById(@PathVariable Long id) {
        return ResponseEntity.ok(libraryService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete library")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
        libraryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
