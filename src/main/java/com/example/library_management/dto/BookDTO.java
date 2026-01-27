package com.example.library_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @Min(1000)
    @Max(2100)
    private Integer publicationYear;

    @NotNull
    private Long libraryId;
}
