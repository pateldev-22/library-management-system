package com.example.library_management.mapper;
import com.example.library_management.dto.LibraryDTO;
import com.example.library_management.entities.Library;
import org.springframework.stereotype.Component;


@Component
public class LibraryMapper {

    public LibraryDTO toDTO(Library library) {
        if (library == null) return null;

        LibraryDTO dto = new LibraryDTO();
        dto.setId((long) library.getID());
        dto.setName(library.getName());
        dto.setAddress(library.getAddress());
        return dto;
    }

    public Library toEntity(LibraryDTO dto) {
        if (dto == null) return null;

        Library library = new Library();
        library.setName(dto.getName());
        library.setAddress(dto.getAddress());
        return library;
    }

    public void updateEntity(Library library, LibraryDTO dto) {
        if (dto == null) return;

        library.setName(dto.getName());
        library.setAddress(dto.getAddress());
    }
}
