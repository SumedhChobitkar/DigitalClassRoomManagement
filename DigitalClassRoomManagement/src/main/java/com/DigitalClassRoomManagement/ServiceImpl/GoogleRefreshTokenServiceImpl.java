package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.GoogleRefreshToken;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Repository.GoogleRefreshTokenRepository;
import com.DigitalClassRoomManagement.Service.GoogleRefreshTokenService;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoogleRefreshTokenServiceImpl implements GoogleRefreshTokenService {
    private static final Logger log = LoggerFactory.getLogger(GoogleRefreshTokenServiceImpl.class);
    private final GoogleRefreshTokenRepository repo;

    public GoogleRefreshTokenServiceImpl(GoogleRefreshTokenRepository repo) { this.repo = repo; }

    @Override
    @Transactional
    public void saveRefreshToken(Teacher teacher, String refreshToken) {
        if (teacher == null || teacher.getId() == null) {
            throw new IllegalArgumentException("Teacher must be persisted before saving refresh token");
        }
        var opt = repo.findByTeacher(teacher);
        GoogleRefreshToken token = opt.orElse(GoogleRefreshToken.builder().teacher(teacher).build());
        token.setRefreshToken(refreshToken);
        repo.save(token);
        log.info("Saved refresh token for teacher {}", teacher.getId());
    }

    @Override
    public GoogleRefreshToken findByTeacher(Teacher teacher) {
        return repo.findByTeacher(teacher).orElse(null);
    }
}
