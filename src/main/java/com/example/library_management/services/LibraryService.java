package com.example.library_management.services;
import com.example.library_management.dto.LibraryDTO;
import java.util.List;

public interface LibraryService extends BaseService<LibraryDTO, Long> {
    LibraryDTO findByName(String name);
    List<LibraryDTO> searchByLocation(String location);
    Long countBooksInLibrary(Long libraryId);
}
