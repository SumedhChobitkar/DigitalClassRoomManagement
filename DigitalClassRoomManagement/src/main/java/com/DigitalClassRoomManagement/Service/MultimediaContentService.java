package com.DigitalClassRoomManagement.Service;
import com.DigitalClassRoomManagement.Dto.MultimediaContentResponseDto;
import com.DigitalClassRoomManagement.Dto.MultimediaMetaUpdateDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface MultimediaContentService {
    MultimediaContentResponseDto uploadFile(String title, String type, MultipartFile file);
    MultimediaContentResponseDto addYoutube(String title, String url);
    Resource downloadFile(Long id); // returns ByteArrayResource
    MultimediaContentResponseDto getOne(Long id);
    List<MultimediaContentResponseDto> listAll(String type); // optional filter
    MultimediaContentResponseDto updateMeta(Long id, MultimediaMetaUpdateDto dto);
    void delete(Long id);
}
