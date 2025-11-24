package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.homework;
import java.util.List;

public interface HomeworkService {

    homework createHomework(homework homework);

    homework getHomeworkById(Long id);

    List<homework> getAllHomeworks();

    homework updateHomework(Long id, homework homework);

    void deleteHomework(Long id);
}
