package com.example.Social_Network.Security.JWT;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponse {
    private String token;
    private String type = "Bearer";
    private UUID id;
    private String email;
    private List<String> roles;
}
