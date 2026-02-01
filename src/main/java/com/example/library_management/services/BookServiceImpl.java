package com.example.library_management.services;
import com.example.library_management.dto.BookDTO;
import com.example.library_management.entities.Author;
import com.example.library_management.entities.Book;
import com.example.library_management.entities.Library;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.mapper.BookMapper;
import com.example.library_management.repos.AuthorRepository;
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

    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository,
                           LibraryRepository libraryRepository,
                           AuthorRepository authorRepository,
                           BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
        log.info("BookServiceImpl initialized");
    }

    @Override
    public BookDTO create(BookDTO dto) {
        log.info("Creating book: {}", dto.getTitle());
        Library library = libraryRepository.findById(dto.getLibraryId())
                .orElseThrow(() -> new ResourceNotFoundException("Library not found with ID: " + dto.getLibraryId()));

        Book book = bookMapper.toEntity(dto);
        book.setLibrary(library);

        Book saved = bookRepository.save(book);
        log.info("Book created with ID: {}", saved.getId());
        return bookMapper.toDTO(saved);
    }

    @Override
    public BookDTO getById(Long id) {
        log.info("Fetching book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
        return bookMapper.toDTO(book);
    }

    @Override
    public Page<BookDTO> getAll(Pageable pageable) {
        log.info("Fetching all books - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return bookRepository.findAll(pageable).map(bookMapper::toDTO);
    }

    @Override
    public BookDTO update(Long id, BookDTO dto) {
        log.info("Updating book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        bookMapper.updateEntity(book, dto);

        if (dto.getLibraryId() != null && !dto.getLibraryId().equals(book.getLibrary().getID())) {
            Library library = libraryRepository.findById(dto.getLibraryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Library not found with ID: " + dto.getLibraryId()));
            book.setLibrary(library);
        }

        Book updated = bookRepository.save(book);
        log.info("Book updated: {}", updated.getTitle());
        return bookMapper.toDTO(updated);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting book with ID: {}", id);
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with ID: " + id);
        }
        bookRepository.deleteById(id);
        log.info("Book deleted successfully");
    }

    // JPQL query
    @Override
    public List<BookDTO> searchByTitle(String title) {
        log.info("Searching books by title: {}", title);
        return bookRepository.searchByTitle(title)
                .stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Native SQL query
    @Override
    public BookDTO findByIsbn(String isbn) {
        log.info("Finding book by ISBN: {}", isbn);
        Book book = bookRepository.findByIsbnNative(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ISBN: " + isbn));
        return bookMapper.toDTO(book);
    }

    // JPA Criteria API
    @Override
    public List<BookDTO> searchBooks(String title, Integer year) {
        log.info("Searching with Criteria API - Title: {}, Year: {}", title, year);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> book = query.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            predicates.add(cb.like(cb.lower(book.get("title")), "%" + title.toLowerCase() + "%"));
        }
        if (year != null) {
            predicates.add(cb.equal(book.get("publicationYear"), year));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(book.get("title")));

        return entityManager.createQuery(query)
                .getResultList()
                .stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> findBooksByLibrary(Long libraryId) {
        log.info("Finding books by library ID: {}", libraryId);
        return bookRepository.findByLibraryId(libraryId)
                .stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> findBooksByAuthor(Long authorId) {
        log.info("Finding books by author ID: {}", authorId);
        return bookRepository.findByAuthorIdNative(authorId)
                .stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO addAuthorToBook(Long bookId, Long authorId) {
        log.info("Adding author {} to book {}", authorId, bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + authorId));

        book.getAuthors().add(author);
        Book updated = bookRepository.save(book);
        log.info("Author added successfully");
        return bookMapper.toDTO(updated);
    }
}
