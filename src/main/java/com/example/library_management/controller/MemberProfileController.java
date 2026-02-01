package com.example.library_management.controller;
import com.example.library_management.dto.MemberProfileDTO;
import com.example.library_management.services.MemberProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member-profiles")
@Tag(name = "Member Profile Management", description = "APIs for managing member profiles")
@Slf4j
public class MemberProfileController {

    @Autowired
    private MemberProfileService memberProfileService;

    @PostMapping
    @Operation(summary = "Create a new member profile")
    public ResponseEntity<MemberProfileDTO> createProfile(@Valid @RequestBody MemberProfileDTO profileDTO) {
        log.info("REST: Creating member profile for member ID: {}", profileDTO.getMemberId());
        MemberProfileDTO created = memberProfileService.create(profileDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all member profiles")
    public ResponseEntity<Page<MemberProfileDTO>> getAllProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("REST: Fetching all member profiles");
        return ResponseEntity.ok(memberProfileService.getAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get member profile by ID")
    public ResponseEntity<MemberProfileDTO> getProfileById(@PathVariable Long id) {
        log.info("REST: Fetching member profile with ID: {}", id);
        return ResponseEntity.ok(memberProfileService.getById(id));
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "Get member profile by member ID")
    public ResponseEntity<MemberProfileDTO> getProfileByMemberId(@PathVariable Long memberId) {
        log.info("REST: Fetching profile for member ID: {}", memberId);
        return ResponseEntity.ok(memberProfileService.findByMemberId(memberId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update member profile")
    public ResponseEntity<MemberProfileDTO> updateProfile(@PathVariable Long id, @Valid @RequestBody MemberProfileDTO profileDTO) {
        log.info("REST: Updating member profile with ID: {}", id);
        return ResponseEntity.ok(memberProfileService.update(id, profileDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete member profile")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        log.info("REST: Deleting member profile with ID: {}", id);
        memberProfileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
