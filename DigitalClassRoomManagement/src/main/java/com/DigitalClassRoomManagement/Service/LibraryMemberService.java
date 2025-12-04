package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.LibraryMember;
import java.util.List;

public interface LibraryMemberService {
    LibraryMember createMember(LibraryMember member);
    LibraryMember getMemberById(Long memberId);
    List<LibraryMember> getAllMembers();
    LibraryMember updateMember(Long memberId, LibraryMember member);
    void deleteMember(Long memberId);
}
