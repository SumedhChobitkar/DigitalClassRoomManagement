package com.DigitalClassRoomManagement.Service;
import com.DigitalClassRoomManagement.Dto.ResultDto;
import com.DigitalClassRoomManagement.Entity.Result;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResultService {

    ResultDto createResult(ResultDto dto);

    ResultDto getResultById(Long id);

    List<ResultDto> getAllResults();

    ResultDto updateResult(Long id, ResultDto dto);

    void deleteResult(Long id);

    List<ResultDto> getResultsByStudent(Long studentId);

    List<ResultDto> getResultsByExam(Long examId);

    List<ResultDto> getResultsByStudentAndExam(Long studentId, Long examId);

    List<ResultDto> getResultsByTopMarks();




}
