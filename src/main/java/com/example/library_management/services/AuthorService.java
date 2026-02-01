package com.example.library_management.services;

import com.example.library_management.dto.AuthorDTO;

import java.util.List;

public interface AuthorService extends BaseService<AuthorDTO,Long>{
    AuthorDTO findByName(String name);
    List<AuthorDTO> searchByName(String name);
}
