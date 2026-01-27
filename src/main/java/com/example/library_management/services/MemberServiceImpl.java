package com.example.library_management.services;

import com.example.library_management.dto.MemberDTO;
import com.example.library_management.entities.Member;
import com.example.library_management.exception.ResourceNotFoundException;
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

    // FIELD INJECTION (Different from BookService)
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public MemberDTO create(MemberDTO dto) {
        log.info("Creating member: {}", dto.getName());
        Member member = convertToEntity(dto);
        Member saved = memberRepository.save(member);
        return convertToDTO(saved);
    }

    @Override
    public MemberDTO getById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        return convertToDTO(member);
    }

    @Override
    public Page<MemberDTO> getAll(Pageable pageable) {
        return memberRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public MemberDTO findByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new ResourceNotFoundException("Member not found");
        }
        return convertToDTO(member);
    }

    @Override
    public List<MemberDTO> findJoinedAfter(LocalDate date) {
        return memberRepository.findMembersJoinedAfter(date)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MemberDTO convertToDTO(Member member) {
        return new MemberDTO(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getMembershipDate()
        );
    }

    private Member convertToEntity(MemberDTO dto) {
        Member member = new Member();
        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setMembershipDate(dto.getMembershipDate());
        return member;
    }
}
