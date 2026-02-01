package com.example.library_management.services;
import com.example.library_management.dto.BookDTO;
import java.util.List;

public interface BookService extends BaseService<BookDTO, Long> {
    List<BookDTO> searchByTitle(String title);
    BookDTO findByIsbn(String isbn);
    List<BookDTO> searchBooks(String title, Integer year);
    List<BookDTO> findBooksByLibrary(Long libraryId);
    List<BookDTO> findBooksByAuthor(Long authorId);
    BookDTO addAuthorToBook(Long bookId, Long authorId);
}
