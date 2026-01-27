package com.example.library_management.entities;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

public class BorrowRecords {
    private Long id;

    private LocalDate borrowDate;
    private LocalDate returnDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

}
