package com.DigitalClassRoomManagement.ServiceImpl;
import com.DigitalClassRoomManagement.Dto.FeeStructureDto;
import com.DigitalClassRoomManagement.Entity.FeeStructure;
import com.DigitalClassRoomManagement.Repository.FeeStructureRepository;
import com.DigitalClassRoomManagement.Service.FeeStructureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@Slf4j
public class FeeStructureServiceImpl implements FeeStructureService {
    private final FeeStructureRepository feeRepo;
    public FeeStructureServiceImpl(FeeStructureRepository feeRepo) {
        this.feeRepo = feeRepo;
    }
    @Override
    public FeeStructure createFee(FeeStructureDto dto) {
        try {
            FeeStructure fee = new FeeStructure();
            fee.setFeeName(dto.getFeeName());
            fee.setAmount(dto.getAmount());
            return feeRepo.save(fee);
        } catch (Exception e) {
            log.error("Error creating fee structure: {}", e.getMessage());
            throw new RuntimeException("Failed to create fee structure.");
        }
    }
    @Override
    public FeeStructure updateFee(Long id, FeeStructureDto dto) {
        try {
            FeeStructure fee = feeRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Fee not found"));
            fee.setFeeName(dto.getFeeName());
            fee.setAmount(dto.getAmount());
            return feeRepo.save(fee);
        } catch (Exception e) {
            log.error("Error updating fee: {}", e.getMessage());
            throw new RuntimeException("Failed to update fee.");
        }
    }

    @Override
    public FeeStructure getFeeById(Long id) {
        try {
            return feeRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Fee not found"));
        } catch (Exception e) {
            log.error("Error fetching fee: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch fee.");
        }
    }

    @Override
    public List<FeeStructure> getAllFees() {
        try {
            return feeRepo.findAll();
        } catch (Exception e) {
            log.error("Error listing fees: {}", e.getMessage());
            throw new RuntimeException("Failed to load fee list.");
        }
    }

    @Override
    public String deleteFee(Long id) {
        try {
            FeeStructure fee = feeRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Fee not found"));
            feeRepo.delete(fee);
            return "Fee deleted successfully";
        } catch (Exception e) {
            log.error("Error deleting fee: {}", e.getMessage());
            throw new RuntimeException("Failed to delete fee.");
        }
    }
}