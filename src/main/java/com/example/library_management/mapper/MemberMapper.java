package com.example.library_management.mapper;
import com.example.library_management.dto.MemberDTO;
import com.example.library_management.entities.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberDTO toDTO(Member member) {
        if (member == null) return null;

        MemberDTO dto = new MemberDTO();
        dto.setId(member.getId());
        dto.setName(member.getName());
        dto.setEmail(member.getEmail());
        dto.setMembershipDate(member.getMembershipDate());
        return dto;
    }

    public Member toEntity(MemberDTO dto) {
        if (dto == null) return null;

        Member member = new Member();
        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setMembershipDate(dto.getMembershipDate());
        return member;
    }

    public void updateEntity(Member member, MemberDTO dto) {
        if (dto == null) return;

        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setMembershipDate(dto.getMembershipDate());
    }
}

