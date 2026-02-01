package com.example.library_management.services;
import com.example.library_management.config.BorrowingStatus;
import com.example.library_management.dto.BorrowRecordDTO;
import com.example.library_management.entities.Book;
import com.example.library_management.entities.BorrowRecord;
import com.example.library_management.entities.Member;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.mapper.BorrowRecordMapper;
import com.example.library_management.repos.BookRepository;
import com.example.library_management.repos.BorrowRecordRepository;
import com.example.library_management.repos.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class BorrowRecordServiceImpl implements BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordMapper borrowRecordMapper;

    @Autowired
    public BorrowRecordServiceImpl(BorrowRecordRepository borrowRecordRepository,
                                   MemberRepository memberRepository,
                                   BookRepository bookRepository,
                                   BorrowRecordMapper borrowRecordMapper) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.borrowRecordMapper = borrowRecordMapper;
        log.info("BorrowRecordServiceImpl initialized");
    }

    @Override
    public BorrowRecordDTO create(BorrowRecordDTO dto) {
        log.info("Creating borrow record");
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + dto.getMemberId()));
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + dto.getBookId()));

        BorrowRecord record = borrowRecordMapper.toEntity(dto);
        record.setMember(member);
        record.setBook(book);

        BorrowRecord saved = borrowRecordRepository.save(record);
        log.info("Borrow record created with ID: {}", saved.getId());
        return borrowRecordMapper.toDTO(saved);
    }

    @Override
    public BorrowRecordDTO getById(Long id) {
        log.info("Fetching borrow record with ID: {}", id);
        BorrowRecord record = borrowRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with ID: " + id));
        return borrowRecordMapper.toDTO(record);
    }

    @Override
    public Page<BorrowRecordDTO> getAll(Pageable pageable) {
        log.info("Fetching all borrow records");
        return borrowRecordRepository.findAll(pageable).map(borrowRecordMapper::toDTO);
    }

    @Override
    public BorrowRecordDTO update(Long id, BorrowRecordDTO dto) {
        log.info("Updating borrow record with ID: {}", id);
        BorrowRecord record = borrowRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with ID: " + id));
        borrowRecordMapper.updateEntity(record, dto);
        BorrowRecord updated = borrowRecordRepository.save(record);
        log.info("Borrow record updated");
        return borrowRecordMapper.toDTO(updated);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting borrow record with ID: {}", id);
        if (!borrowRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("Borrow record not found with ID: " + id);
        }
        borrowRecordRepository.deleteById(id);
        log.info("Borrow record deleted successfully");
    }

    @Override
    public List<BorrowRecordDTO> findByMemberId(Long memberId) {
        log.info("Finding borrow records for member ID: {}", memberId);
        return borrowRecordRepository.findByMemberId(memberId)
                .stream()
                .map(borrowRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowRecordDTO> findByBookId(Long bookId) {
        log.info("Finding borrow records for book ID: {}", bookId);
        return borrowRecordRepository.findByBookId(bookId)
                .stream()
                .map(borrowRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowRecordDTO> findByStatus(BorrowingStatus status) {
        log.info("Finding borrow records with status: {}", status);
        return borrowRecordRepository.findByStatus(status)
                .stream()
                .map(borrowRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowRecordDTO> findOverdueRecords(LocalDate sinceDate) {
        log.info("Finding overdue records since: {}", sinceDate);
        return borrowRecordRepository.findOverdueRecords(sinceDate)
                .stream()
                .map(borrowRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BorrowRecordDTO borrowBook(Long memberId, Long bookId) {
        log.info("Member {} borrowing book {}", memberId, bookId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + memberId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));

        BorrowRecord record = new BorrowRecord();
        record.setMember(member);
        record.setBook(book);
        record.setBorrowDate(LocalDate.now());
        record.setBorrowingStatus(BorrowingStatus.BORROWED);

        BorrowRecord saved = borrowRecordRepository.save(record);
        log.info("Book borrowed successfully, record ID: {}", saved.getId());
        return borrowRecordMapper.toDTO(saved);
    }

    @Override
    public BorrowRecordDTO returnBook(Long borrowRecordId) {
        log.info("Returning book for borrow record ID: {}", borrowRecordId);
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with ID: " + borrowRecordId));

        record.setReturnDate(LocalDate.now());
        record.setBorrowingStatus(BorrowingStatus.RETURNED);

        BorrowRecord updated = borrowRecordRepository.save(record);
        log.info("Book returned successfully");
        return borrowRecordMapper.toDTO(updated);
    }
}
