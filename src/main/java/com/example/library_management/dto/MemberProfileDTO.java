package com.example.library_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileDTO {

    private Long id;

    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    @Size(max = 100, message = "Occupation cannot exceed 100 characters")
    private String occupation;

    @NotNull(message = "Member ID is required")
    private Long memberId;
}