package com.example.library_management.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BaseService<T, ID> {
    T create(T dto);
    T getById(ID id);
    Page<T> getAll(Pageable pageable);
    void deleteById(ID id);
}
