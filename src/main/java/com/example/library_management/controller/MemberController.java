package com.example.library_management.controller;
import com.example.library_management.dto.MemberDTO;
import com.example.library_management.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member Management", description = "APIs for managing library members")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping
    @Operation(summary = "Create a new member")
    public ResponseEntity<MemberDTO> createMember(@Valid @RequestBody MemberDTO memberDTO) {
        log.info("REST: Creating member: {}", memberDTO.getName());
        MemberDTO created = memberService.create(memberDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all members with pagination")
    public ResponseEntity<Page<MemberDTO>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        log.info("REST: Fetching all members");
        return ResponseEntity.ok(memberService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get member by ID")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long id) {
        log.info("REST: Fetching member with ID: {}", id);
        return ResponseEntity.ok(memberService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update member")
    public ResponseEntity<MemberDTO> updateMember(@PathVariable Long id, @Valid @RequestBody MemberDTO memberDTO) {
        log.info("REST: Updating member with ID: {}", id);
        return ResponseEntity.ok(memberService.update(id, memberDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete member")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        log.info("REST: Deleting member with ID: {}", id);
        memberService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Find member by email")
    public ResponseEntity<MemberDTO> findByEmail(@PathVariable String email) {
        log.info("REST: Finding member by email: {}", email);
        return ResponseEntity.ok(memberService.findByEmail(email));
    }

    @GetMapping("/joined-after")
    @Operation(summary = "Find members who joined after a date")
    public ResponseEntity<List<MemberDTO>> findJoinedAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("REST: Finding members joined after: {}", date);
        return ResponseEntity.ok(memberService.findJoinedAfter(date));
    }

    @GetMapping("/search")
    @Operation(summary = "Search members by name")
    public ResponseEntity<List<MemberDTO>> searchByName(@RequestParam String name) {
        log.info("REST: Searching members by name: {}", name);
        return ResponseEntity.ok(memberService.searchByName(name));
    }

    @GetMapping("/count-active")
    @Operation(summary = "Count active members since a date")
    public ResponseEntity<Long> countActiveMembersSince(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("REST: Counting active members since: {}", date);
        return ResponseEntity.ok(memberService.countActiveMembersSince(date));
    }
}
