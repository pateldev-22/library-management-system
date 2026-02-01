package com.example.library_management.services;
import com.example.library_management.config.BorrowingStatus;
import com.example.library_management.dto.BorrowRecordDTO;

import java.time.LocalDate;
import java.util.List;

public interface BorrowRecordService extends BaseService<BorrowRecordDTO, Long> {
    List<BorrowRecordDTO> findByMemberId(Long memberId);
    List<BorrowRecordDTO> findByBookId(Long bookId);
    List<BorrowRecordDTO> findByStatus(BorrowingStatus status);
    List<BorrowRecordDTO> findOverdueRecords(LocalDate sinceDate);
    BorrowRecordDTO borrowBook(Long memberId, Long bookId);
    BorrowRecordDTO returnBook(Long borrowRecordId);
}
