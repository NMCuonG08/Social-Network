package com.example.Social_Network.Security.JWT;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class JWTController {

    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public String getTokenForAuthentication(@RequestBody JWTAuthenticationRequest authRequest){
        Authentication authentication= authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                        authRequest.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.getGeneratedToken(authRequest.getEmail());
        }
        else {
            throw  new UsernameNotFoundException("Invalid User credentials");
        }
    }
}
