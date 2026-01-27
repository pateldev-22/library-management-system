package com.example.library_management.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "libraries")
@Getter
@Setter
@ToString(exclude = "books")
@EqualsAndHashCode(exclude = "books")
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private String name;
    private String Address;

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

}
