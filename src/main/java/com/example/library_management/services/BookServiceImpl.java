package com.example.library_management.services;

import com.example.library_management.dto.BookDTO;
import com.example.library_management.entities.Book;
import com.example.library_management. entities.Library;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repos.BookRepository;
import com.example.library_management.repos.LibraryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class BookServiceImpl implements BookService {

    // CONSTRUCTOR INJECTION
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, LibraryRepository libraryRepository) {
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
        log.info("BookServiceImpl initialized");
    }

    // POLYMORPHISM - Overriding interface methods
    @Override
    public BookDTO create(BookDTO dto) {
        log.info("Creating book: {}", dto.getTitle());

        Library library = libraryRepository.findById(dto.getLibraryId())
                .orElseThrow(() -> new ResourceNotFoundException("Library not found"));

        Book book = convertToEntity(dto);
        book.setLibrary(library);

        Book saved = bookRepository.save(book);
        return convertToDTO(saved);
    }

    @Override
    public BookDTO getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        return convertToDTO(book);
    }

    @Override
    public Page<BookDTO> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found");
        }
        bookRepository.deleteById(id);
    }

    // JPQL Query
    @Override
    public List<BookDTO> searchByTitle(String title) {
        log.info("Searching books by title: {}", title);
        return bookRepository.searchByTitle(title)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Native SQL Query
    @Override
    public BookDTO findByIsbn(String isbn) {
        log.info("Finding book by ISBN: {}", isbn);
        Book book = bookRepository.findByIsbnNative(isbn);
        if (book == null) {
            throw new ResourceNotFoundException("Book not found");
        }
        return convertToDTO(book);
    }

    // JPA Criteria API
    @Override
    public List<BookDTO> searchBooks(String title, Integer year) {
        log.info("Searching with Criteria API - Title: {}, Year: {}", title, year);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> book = query.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();

        if (title != null) {
            predicates.add(cb.like(book.get("title"), "%" + title + "%"));
        }

        if (year != null) {
            predicates.add(cb.equal(book.get("publicationYear"), year));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query)
                .getResultList()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setLibraryId((long) book.getLibrary().getID());
        return dto;
    }

    private Book convertToEntity(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationYear(dto.getPublicationYear());
        return book;
    }
}
