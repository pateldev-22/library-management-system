package com.example.library_management.repos;
import com.example.library_management.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // JPQL
    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

    // Native SQL
    @Query(value = "SELECT * FROM members WHERE membership_date >= :date", nativeQuery = true)
    List<Member> findMembersJoinedAfter(@Param("date") LocalDate date);

    // JPQL
    @Query("SELECT m FROM Member m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Member> searchByName(@Param("name") String name);

    // JPQL
    @Query("SELECT COUNT(m) FROM Member m WHERE m.membershipDate >= :date")
    Long countActiveMembersSince(@Param("date") LocalDate date);
}