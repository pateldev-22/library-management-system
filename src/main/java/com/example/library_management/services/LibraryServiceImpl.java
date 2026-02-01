package com.example.library_management.services;
import com.example.library_management.dto.LibraryDTO;
import com.example.library_management.entities.Library;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.mapper.LibraryMapper;
import com.example.library_management.repos.BookRepository;
import com.example.library_management.repos.LibraryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Transactional
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;
    private final LibraryMapper libraryMapper;

    @Autowired
    public LibraryServiceImpl(LibraryRepository libraryRepository,
                              BookRepository bookRepository,
                              LibraryMapper libraryMapper) {
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
        this.libraryMapper = libraryMapper;
        log.info("LibraryServiceImpl initialized");
    }

    @Override
    public LibraryDTO create(LibraryDTO dto) {
        log.info("Creating library: {}", dto.getName());
        Library library = libraryMapper.toEntity(dto);
        Library saved = libraryRepository.save(library);
        log.info("Library created with ID: {}", saved.getID());
        return libraryMapper.toDTO(saved);
    }

    @Override
    public LibraryDTO getById(Long id) {
        log.info("Fetching library with ID: {}", id);
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Library not found with ID: " + id));
        return libraryMapper.toDTO(library);
    }

    @Override
    public Page<LibraryDTO> getAll(Pageable pageable) {
        log.info("Fetching all libraries - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return libraryRepository.findAll(pageable).map(libraryMapper::toDTO);
    }

    @Override
    public LibraryDTO update(Long id, LibraryDTO dto) {
        log.info("Updating library with ID: {}", id);
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Library not found with ID: " + id));
        libraryMapper.updateEntity(library, dto);
        Library updated = libraryRepository.save(library);
        log.info("Library updated: {}", updated.getName());
        return libraryMapper.toDTO(updated);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting library with ID: {}", id);
        if (!libraryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Library not found with ID: " + id);
        }
        libraryRepository.deleteById(id);
        log.info("Library deleted successfully");
    }

    @Override
    public LibraryDTO findByName(String name) {
        log.info("Finding library by name: {}", name);
        Library library = libraryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Library not found with name: " + name));
        return libraryMapper.toDTO(library);
    }

    @Override
    public List<LibraryDTO> searchByLocation(String location) {
        log.info("Searching libraries by location: {}", location);
        return libraryRepository.findByLocation(location)
                .map(library -> Collections.singletonList(libraryMapper.toDTO(library)))
                .orElse(Collections.emptyList());
    }

    @Override
    public Long countBooksInLibrary(Long libraryId) {
        log.info("Counting books in library: {}", libraryId);
        return bookRepository.countByLibraryId(libraryId);
    }
}
