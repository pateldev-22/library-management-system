package com.example.library_management.services;
import com.example.library_management.dto.AuthorDTO;
import com.example.library_management.entities.Author;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.mapper.AuthorMapper;
import com.example.library_management.repos.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    public AuthorDTO create(AuthorDTO dto) {
        log.info("Creating author: {}", dto.getName());
        Author author = authorMapper.toEntity(dto);
        Author saved = authorRepository.save(author);
        log.info("Author created with ID: {}", saved.getId());
        return authorMapper.toDTO(saved);
    }

    @Override
    public AuthorDTO getById(Long id) {
        log.info("Fetching author with ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + id));
        return authorMapper.toDTO(author);
    }

    @Override
    public Page<AuthorDTO> getAll(Pageable pageable) {
        log.info("Fetching all authors");
        return authorRepository.findAll(pageable).map(authorMapper::toDTO);
    }

    @Override
    public AuthorDTO update(Long id, AuthorDTO dto) {
        log.info("Updating author with ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + id));
        authorMapper.updateEntity(author, dto);
        Author updated = authorRepository.save(author);
        log.info("Author updated: {}", updated.getName());
        return authorMapper.toDTO(updated);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting author with ID: {}", id);
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with ID: " + id);
        }
        authorRepository.deleteById(id);
        log.info("Author deleted successfully");
    }

    @Override
    public AuthorDTO findByName(String name) {
        log.info("Finding author by name: {}", name);
        Author author = authorRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with name: " + name));
        return authorMapper.toDTO(author);
    }

    @Override
    public List<AuthorDTO> searchByName(String name) {
        log.info("Searching authors by name: {}", name);
        return authorRepository.searchByName(name)
                .stream()
                .map(authorMapper::toDTO)
                .collect(Collectors.toList());
    }
}
