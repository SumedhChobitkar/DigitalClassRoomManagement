package com.DigitalClassRoomManagement.Service;

import java.util.Map;

public interface GoogleTokenService {
    Map<String, Object> exchangeCodeForTokens(String code, String redirectUri);
    String getAccessTokenFromRefreshToken(String refreshToken);
}
