package com.example.library_management.entities;

import jakarta.persistence.*;

import java.util.*;

public class Book {

    private long Id;

    private String title;
    private String isbn;
    private Date publication_date;

    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;


    @ManyToMany
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id"),inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BorrowRecords> borrowRecordsList = new ArrayList<>();
}
