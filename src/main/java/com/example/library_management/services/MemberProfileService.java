package com.example.library_management.services;
import com.example.library_management.dto.MemberProfileDTO;

public interface MemberProfileService extends BaseService<MemberProfileDTO, Long> {
    MemberProfileDTO findByMemberId(Long memberId);
}
