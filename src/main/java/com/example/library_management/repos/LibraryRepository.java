package com.example.library_management.repos;
import com.example.library_management.entities.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {

    // JPQL
    @Query("SELECT l FROM Library l WHERE l.name = :name")
    Optional<Library> findByName(@Param("name") String name);

    // Native SQL
    @Query(value = "SELECT * FROM libraries WHERE address LIKE %:location%", nativeQuery = true)
    Optional<Library> findByLocation(@Param("location") String location);
}

