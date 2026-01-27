package com.example.library_management.repos;

import com.example.library_management.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // JPQL Query
    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Member findByEmail(@Param("email") String email);

    // Native SQL Query
    @Query(value = "SELECT * FROM members WHERE membership_date >= :date", nativeQuery = true)
    List<Member> findMembersJoinedAfter(@Param("date") LocalDate date);
}

