package com.example.library_management.mapper;
import com.example.library_management.dto.MemberProfileDTO;
import com.example.library_management.entities.MemberProfile;
import org.springframework.stereotype.Component;

@Component
public class MemberProfileMapper {

    public MemberProfileDTO toDTO(MemberProfile profile) {
        if (profile == null) return null;

        MemberProfileDTO dto = new MemberProfileDTO();
        dto.setId(profile.getId());
        dto.setAddress(profile.getAddress());
        dto.setPhoneNumber(profile.getPhoneNumber());
        dto.setOccupation(profile.getOccupation());

        if (profile.getMember() != null) {
            dto.setMemberId(profile.getMember().getId());
        }

        return dto;
    }

    public MemberProfile toEntity(MemberProfileDTO dto) {
        if (dto == null) return null;

        MemberProfile profile = new MemberProfile();
        profile.setAddress(dto.getAddress());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setOccupation(dto.getOccupation());
        return profile;
    }

    public void updateEntity(MemberProfile profile, MemberProfileDTO dto) {
        if (dto == null) return;

        profile.setAddress(dto.getAddress());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setOccupation(dto.getOccupation());
    }
}
