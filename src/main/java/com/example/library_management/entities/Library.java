package com.example.library_management.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libraries")
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private String name;
    private String Address;

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();
}
