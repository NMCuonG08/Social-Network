package com.example.Social_Network.Security.JWT;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class JWTAuthenticationRequest {
    private String email;
    private String password;
}
