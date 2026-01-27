package com.example.library_management.services;

import com.example.library_management.dto.LibraryDTO;
import com.example.library_management.entities.Library;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repos.LibraryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class LibraryServiceImpl implements LibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    @Override
    public LibraryDTO create(LibraryDTO dto) {
        log.info("Creating library: {}", dto.getName());
        Library library = convertToEntity(dto);
        Library saved = libraryRepository.save(library);
        return convertToDTO(saved);
    }

    @Override
    public LibraryDTO getById(Long id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Library not found"));
        return convertToDTO(library);
    }

    @Override
    public Page<LibraryDTO> getAll(Pageable pageable) {
        return libraryRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public void deleteById(Long id) {
        libraryRepository.deleteById(id);
    }

    @Override
    public LibraryDTO findByName(String name) {
        // Implementation if needed
        return null;
    }

    private LibraryDTO convertToDTO(Library library) {
        return new LibraryDTO(
                (long) library.getID(),
                library.getName(),
                library.getAddress()
        );
    }

    private Library convertToEntity(LibraryDTO dto) {
        Library library = new Library();
        library.setName(dto.getName());
        library.setAddress(dto.getAddress());
        return library;
    }
}
