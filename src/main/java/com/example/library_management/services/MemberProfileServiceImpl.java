package com.example.library_management.services;
import com.example.library_management.dto.MemberProfileDTO;
import com.example.library_management.entities.Member;
import com.example.library_management.entities.MemberProfile;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.mapper.MemberProfileMapper;
import com.example.library_management.repos.MemberProfileRepository;
import com.example.library_management.repos.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// CONSTRUCTOR INJECTION
@Service
@Slf4j
@Transactional
public class MemberProfileServiceImpl implements MemberProfileService {

    private final MemberProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final MemberProfileMapper profileMapper;

    @Autowired
    public MemberProfileServiceImpl(MemberProfileRepository profileRepository,
                                    MemberRepository memberRepository,
                                    MemberProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.memberRepository = memberRepository;
        this.profileMapper = profileMapper;
        log.info("MemberProfileServiceImpl initialized");
    }

    @Override
    public MemberProfileDTO create(MemberProfileDTO dto) {
        log.info("Creating member profile for member ID: {}", dto.getMemberId());
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + dto.getMemberId()));

        MemberProfile profile = profileMapper.toEntity(dto);
        profile.setMember(member);

        MemberProfile saved = profileRepository.save(profile);
        log.info("Member profile created with ID: {}", saved.getId());
        return profileMapper.toDTO(saved);
    }

    @Override
    public MemberProfileDTO getById(Long id) {
        log.info("Fetching member profile with ID: {}", id);
        MemberProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member profile not found with ID: " + id));
        return profileMapper.toDTO(profile);
    }

    @Override
    public Page<MemberProfileDTO> getAll(Pageable pageable) {
        log.info("Fetching all member profiles");
        return profileRepository.findAll(pageable).map(profileMapper::toDTO);
    }

    @Override
    public MemberProfileDTO update(Long id, MemberProfileDTO dto) {
        log.info("Updating member profile with ID: {}", id);
        MemberProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member profile not found with ID: " + id));
        profileMapper.updateEntity(profile, dto);
        MemberProfile updated = profileRepository.save(profile);
        log.info("Member profile updated");
        return profileMapper.toDTO(updated);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting member profile with ID: {}", id);
        if (!profileRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member profile not found with ID: " + id);
        }
        profileRepository.deleteById(id);
        log.info("Member profile deleted successfully");
    }

    @Override
    public MemberProfileDTO findByMemberId(Long memberId) {
        log.info("Finding member profile by member ID: {}", memberId);
        MemberProfile profile = profileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member profile not found for member ID: " + memberId));
        return profileMapper.toDTO(profile);
    }
}
