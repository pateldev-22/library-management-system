package com.example.library_management.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "membership_date", nullable = false)
    private LocalDate membershipDate;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberProfile memberProfile;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<BorrowRecord> borrowRecords = new ArrayList<>();
}
