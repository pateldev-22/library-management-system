package com.example.library_management.repos;


import com.example.library_management.entities.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {

    // JPQL
    @Query("SELECT mp FROM MemberProfile mp WHERE mp.member.id = :memberId")
    Optional<MemberProfile> findByMemberId(@Param("memberId") Long memberId);
}
