package com.example.library_management.mapper;

import com.example.library_management.dto.BorrowRecordDTO;
import com.example.library_management.entities.BorrowRecord;
import org.springframework.stereotype.Component;

@Component
public class BorrowRecordMapper {


    public BorrowRecordDTO toDTO(BorrowRecord record) {
        if (record == null) return null;

        BorrowRecordDTO dto = new BorrowRecordDTO();
        dto.setId(record.getId());
        dto.setBorrowDate(record.getBorrowDate());
        dto.setReturnDate(record.getReturnDate());
        dto.setStatus(record.getBorrowingStatus());

        // Pull member info
        if (record.getMember() != null) {
            dto.setMemberId(record.getMember().getId());
            dto.setMemberName(record.getMember().getName());
        }

        // Pull book info
        if (record.getBook() != null) {
            dto.setBookId(record.getBook().getId());
            dto.setBookTitle(record.getBook().getTitle());
        }

        return dto;
    }

    public BorrowRecord toEntity(BorrowRecordDTO dto) {
        if (dto == null) return null;

        BorrowRecord record = new BorrowRecord();
        record.setBorrowDate(dto.getBorrowDate());
        record.setReturnDate(dto.getReturnDate());
        record.setBorrowingStatus(dto.getStatus());
        return record;
    }

    public void updateEntity(BorrowRecord record, BorrowRecordDTO dto) {
        if (dto == null) return;

        record.setBorrowDate(dto.getBorrowDate());
        record.setReturnDate(dto.getReturnDate());
        record.setBorrowingStatus(dto.getStatus());
    }
}
