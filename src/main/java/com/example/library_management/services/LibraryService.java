package com.example.library_management.services;

import com.example.library_management.dto.LibraryDTO;

public interface LibraryService extends BaseService<LibraryDTO, Long> {
    LibraryDTO findByName(String name);
}
