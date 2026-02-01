package com.example.library_management.repos;
import com.example.library_management.config.BorrowingStatus;
import com.example.library_management.entities.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    // JPQL
    @Query("SELECT br FROM BorrowRecord br WHERE br.member.id = :memberId")
    List<BorrowRecord> findByMemberId(@Param("memberId") Long memberId);

    // JPQL
    @Query("SELECT br FROM BorrowRecord br WHERE br.book.id = :bookId")
    List<BorrowRecord> findByBookId(@Param("bookId") Long bookId);

    // JPQL
    @Query("SELECT br FROM BorrowRecord br WHERE br.borrowingStatus = :borrowingStatus")
    List<BorrowRecord> findByStatus(@Param("borrowingStatus") BorrowingStatus borrowingStatus);

    // Native SQL
    @Query(value = "SELECT * FROM borrow_records WHERE borrowing_status = 'BORROWED' AND borrow_date < :date",
            nativeQuery = true)
    List<BorrowRecord> findOverdueRecords(@Param("date") LocalDate date);

    // JPQL
    @Query("SELECT br FROM BorrowRecord br WHERE br.member.id = :memberId AND br.borrowingStatus = :borrowingStatus")
    List<BorrowRecord> findActiveBorrowsByMember(@Param("memberId") Long memberId,
                                                 @Param("borrowingStatus") BorrowingStatus borrowingStatus);
}
