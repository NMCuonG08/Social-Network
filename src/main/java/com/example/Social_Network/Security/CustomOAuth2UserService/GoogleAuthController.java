package com.example.Social_Network.Security.CustomOAuth2UserService;

import com.example.Social_Network.Security.JWT.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final JWTService jwtService;
    private final RestTemplate restTemplate = new RestTemplate();  // Using RestTemplate to call Google API

    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String CLIENT_ID = "770516629356-oq4r6265tsvftmu6of4br09cbaqan9en.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8080/login/oauth2/code/google";

    @GetMapping("/token")
    public String getToken(@RequestParam("code") String code) {
        // Prepare token request payload
        String requestUrl = GOOGLE_TOKEN_URL + "?code=" + code +
                "&client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&redirect_uri=" + REDIRECT_URI +
                "&grant_type=authorization_code";

        // Send POST request to Google Token Endpoint
        Map<String, String> response = restTemplate.postForObject(requestUrl, null, Map.class);

        if (response != null && response.containsKey("access_token")) {
            String accessToken = response.get("access_token");
            // You can generate your JWT or use the access token directly
            // For demonstration, return the access token
            return accessToken;
        } else {
            throw new RuntimeException("Failed to retrieve access token from Google");
        }
    }
}