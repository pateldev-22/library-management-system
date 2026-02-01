package com.example.library_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(?:\\d{10}|\\d{13})$", message = "ISBN must be 10 or 13 digits")
    private String isbn;

    @NotNull(message = "Publication year is required")
    @Min(value = 1000, message = "Publication year must be at least 1000")
    @Max(value = 2100, message = "Publication year cannot exceed 2100")
    private Integer publicationYear;

    @NotNull(message = "Library ID is required")
    private Long libraryId;

    private String libraryName;

    private Set<Long> authorIds = new HashSet<>();
}
