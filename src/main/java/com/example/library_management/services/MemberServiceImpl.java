package com.example.library_management.services;
import com.example.library_management.dto.MemberDTO;
import com.example.library_management.entities.Member;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.mapper.MemberMapper;
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
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public MemberDTO create(MemberDTO dto) {
        log.info("Creating member: {}", dto.getName());
        Member member = memberMapper.toEntity(dto);
        Member saved = memberRepository.save(member);
        log.info("Member created with ID: {}", saved.getId());
        return memberMapper.toDTO(saved);
    }

    @Override
    public MemberDTO getById(Long id) {
        log.info("Fetching member with ID: {}", id);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + id));
        return memberMapper.toDTO(member);
    }

    @Override
    public Page<MemberDTO> getAll(Pageable pageable) {
        log.info("Fetching all members");
        return memberRepository.findAll(pageable).map(memberMapper::toDTO);
    }

    @Override
    public MemberDTO update(Long id, MemberDTO dto) {
        log.info("Updating member with ID: {}", id);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + id));
        memberMapper.updateEntity(member, dto);
        Member updated = memberRepository.save(member);
        log.info("Member updated: {}", updated.getName());
        return memberMapper.toDTO(updated);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting member with ID: {}", id);
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member not found with ID: " + id);
        }
        memberRepository.deleteById(id);
        log.info("Member deleted successfully");
    }

    @Override
    public MemberDTO findByEmail(String email) {
        log.info("Finding member by email: {}", email);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with email: " + email));
        return memberMapper.toDTO(member);
    }

    @Override
    public List<MemberDTO> findJoinedAfter(LocalDate date) {
        log.info("Finding members joined after: {}", date);
        return memberRepository.findMembersJoinedAfter(date)
                .stream()
                .map(memberMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MemberDTO> searchByName(String name) {
        log.info("Searching members by name: {}", name);
        return memberRepository.searchByName(name)
                .stream()
                .map(memberMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long countActiveMembersSince(LocalDate date) {
        log.info("Counting active members since: {}", date);
        return memberRepository.countActiveMembersSince(date);
    }
}
