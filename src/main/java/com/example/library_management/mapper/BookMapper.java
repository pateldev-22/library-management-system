package com.example.library_management.mapper;
import com.example.library_management.dto.BookDTO;
import com.example.library_management.entities.Author;
import com.example.library_management.entities.Book;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookDTO toDTO(Book book) {
        if (book == null) return null;

        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationYear(book.getPublicationYear());

        if (book.getLibrary() != null) {
            dto.setLibraryId((long) book.getLibrary().getID());
            dto.setLibraryName(book.getLibrary().getName());
        }

        if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
            dto.setAuthorIds(
                    book.getAuthors().stream()
                            .map(Author::getId)
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }

    public Book toEntity(BookDTO dto) {
        if (dto == null) return null;

        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationYear(dto.getPublicationYear());
        return book;
    }

    public void updateEntity(Book book, BookDTO dto) {
        if (dto == null) return;

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationYear(dto.getPublicationYear());
    }
}
