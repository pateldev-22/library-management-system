package com.example.library_management.dto;

import com.example.library_management.config.BorrowingStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecordDTO {

    private Long id;

    @NotNull(message = "Borrow date is required")
    @PastOrPresent(message = "Borrow date cannot be in the future")
    private LocalDate borrowDate;

    private LocalDate returnDate;

    @NotNull(message = "Status is required")
    private BorrowingStatus status;

    @NotNull(message = "Member ID is required")
    private Long memberId;

    private String memberName;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    private String bookTitle;
}
