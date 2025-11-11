package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Dto.AdminResponseDTO;
import com.DigitalClassRoomManagement.Entity.UnAppproveAdmins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnApproveAdminRepo extends JpaRepository<UnAppproveAdmins,Long> {
    @Query("SELECT new com.DigitalClassRoomManagement.Dto.AdminResponseDTO(u.adminId, u.username) FROM UnAppproveAdmins u")
    public List<AdminResponseDTO> getALlUnAppproveAdmins();

}
