package com.example.library_management.services;

import com.example.library_management.dto.MemberDTO;
import java.time.LocalDate;
import java.util.List;

public interface MemberService extends BaseService<MemberDTO, Long> {
    MemberDTO findByEmail(String email);
    List<MemberDTO> findJoinedAfter(LocalDate date);
}
