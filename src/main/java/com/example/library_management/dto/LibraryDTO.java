package com.example.library_management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryDTO {
    private Long id;
    @NotBlank(message = "Library name is required")
    private String name;
    @NotBlank(message = "Address is required")
    private String address;
}
