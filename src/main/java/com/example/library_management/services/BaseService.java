package com.example.library_management.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseService<T, ID> {
    T create(T dto);
    T getById(ID id);
    Page<T> getAll(Pageable pageable);
    T update(ID id, T dto);
    void deleteById(ID id);
}

