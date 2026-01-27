package com.example.library_management.entities;

import jakarta.persistence.*;

public class BookAuthors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book books;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Author authors;

}
