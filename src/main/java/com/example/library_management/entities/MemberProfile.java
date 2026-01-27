package com.example.library_management.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "member_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private String phoneNumber;
    private String occupation;

    @OneToOne
    @JoinColumn(name = "member_id", unique = true, nullable = false)
    @JsonIgnore
    private Member member;
}

