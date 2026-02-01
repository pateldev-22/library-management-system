package com.example.library_management.controller;
import com.example.library_management.config.BorrowingStatus;
import com.example.library_management.dto.BorrowRecordDTO;
import com.example.library_management.services.BorrowRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/borrow-records")
@Tag(name = "Borrow Record Management", description = "APIs for managing book borrowing")
@Slf4j
public class BorrowRecordController {

    @Autowired
    private BorrowRecordService borrowRecordService;

    @PostMapping
    @Operation(summary = "Create a borrow record manually")
    public ResponseEntity<BorrowRecordDTO> createRecord(@Valid @RequestBody BorrowRecordDTO recordDTO) {
        log.info("REST: Creating borrow record");
        BorrowRecordDTO created = borrowRecordService.create(recordDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all borrow records")
    public ResponseEntity<Page<BorrowRecordDTO>> getAllRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("REST: Fetching all borrow records");
        return ResponseEntity.ok(borrowRecordService.getAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get borrow record by ID")
    public ResponseEntity<BorrowRecordDTO> getRecordById(@PathVariable Long id) {
        log.info("REST: Fetching borrow record with ID: {}", id);
        return ResponseEntity.ok(borrowRecordService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update borrow record")
    public ResponseEntity<BorrowRecordDTO> updateRecord(@PathVariable Long id, @Valid @RequestBody BorrowRecordDTO recordDTO) {
        log.info("REST: Updating borrow record with ID: {}", id);
        return ResponseEntity.ok(borrowRecordService.update(id, recordDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete borrow record")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        log.info("REST: Deleting borrow record with ID: {}", id);
        borrowRecordService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "Get all borrow records for a member")
    public ResponseEntity<List<BorrowRecordDTO>> getRecordsByMember(@PathVariable Long memberId) {
        log.info("REST: Fetching borrow records for member: {}", memberId);
        return ResponseEntity.ok(borrowRecordService.findByMemberId(memberId));
    }

    @GetMapping("/book/{bookId}")
    @Operation(summary = "Get all borrow records for a book")
    public ResponseEntity<List<BorrowRecordDTO>> getRecordsByBook(@PathVariable Long bookId) {
        log.info("REST: Fetching borrow records for book: {}", bookId);
        return ResponseEntity.ok(borrowRecordService.findByBookId(bookId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get borrow records by status (BORROWED / RETURNED / OVERDUE)")
    public ResponseEntity<List<BorrowRecordDTO>> getRecordsByStatus(@PathVariable BorrowingStatus status) {
        log.info("REST: Fetching borrow records with status: {}", status);
        return ResponseEntity.ok(borrowRecordService.findByStatus(status));
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get all overdue borrow records since a date")
    public ResponseEntity<List<BorrowRecordDTO>> getOverdueRecords(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sinceDate) {
        log.info("REST: Fetching overdue records since: {}", sinceDate);
        return ResponseEntity.ok(borrowRecordService.findOverdueRecords(sinceDate));
    }

    @PostMapping("/borrow")
    @Operation(summary = "Borrow a book — auto-sets today as borrow date and status = BORROWED")
    public ResponseEntity<BorrowRecordDTO> borrowBook(
            @RequestParam Long memberId,
            @RequestParam Long bookId) {
        log.info("REST: Member {} borrowing book {}", memberId, bookId);
        BorrowRecordDTO record = borrowRecordService.borrowBook(memberId, bookId);
        return new ResponseEntity<>(record, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/return")
    @Operation(summary = "Return a book — auto-sets today as return date and status = RETURNED")
    public ResponseEntity<BorrowRecordDTO> returnBook(@PathVariable Long id) {
        log.info("REST: Returning book for record: {}", id);
        return ResponseEntity.ok(borrowRecordService.returnBook(id));
    }
}
