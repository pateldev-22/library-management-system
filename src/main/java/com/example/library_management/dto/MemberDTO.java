package com.example.library_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private LocalDate membershipDate;
}
