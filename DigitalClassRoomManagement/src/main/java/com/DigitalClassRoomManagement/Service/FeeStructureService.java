package com.DigitalClassRoomManagement.Service;
import com.DigitalClassRoomManagement.Dto.FeeStructureDto;
import com.DigitalClassRoomManagement.Entity.FeeStructure;
import java.util.List;
public interface FeeStructureService {
    FeeStructure createFee(FeeStructureDto dto);
    FeeStructure updateFee(Long id, FeeStructureDto dto);
    FeeStructure getFeeById(Long id);
    List<FeeStructure> getAllFees();
    String deleteFee(Long id);
}