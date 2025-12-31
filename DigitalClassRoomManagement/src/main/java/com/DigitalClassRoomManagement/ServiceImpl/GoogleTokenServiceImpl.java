package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Service.GoogleTokenService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Service
public class GoogleTokenServiceImpl implements GoogleTokenService {
    private static final Logger log = LoggerFactory.getLogger(GoogleTokenServiceImpl.class);
    private final RestTemplate rest = new RestTemplate();

    @Value("${google.client.id}") private String clientId;
    @Value("${google.client.secret}") private String clientSecret;

    @Override
    public Map<String, Object> exchangeCodeForTokens(String code, String redirectUri) {
        String url = "https://oauth2.googleapis.com/token";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = rest.postForEntity(url, request, Map.class);
        log.debug("Token exchange response: status={}, bodyKeys={}", response.getStatusCode(), response.getBody()!=null ? response.getBody().keySet() : null);
        return response.getBody();
    }

    @Override
    public String getAccessTokenFromRefreshToken(String refreshToken) {
        String url = "https://oauth2.googleapis.com/token";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = rest.postForEntity(url, request, Map.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody()!=null) {
            Object token = response.getBody().get("access_token");
            return token != null ? token.toString() : null;
        }
        return null;
    }
}
