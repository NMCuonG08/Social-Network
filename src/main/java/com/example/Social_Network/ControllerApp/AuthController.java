package com.example.Social_Network.ControllerApp;

import com.example.Social_Network.Role.RoleRepository;
import com.example.Social_Network.Security.JWT.JWTAuthenticationRequest;
import com.example.Social_Network.Security.JWT.JWTResponse;
import com.example.Social_Network.Security.JWT.JWTService;
import com.example.Social_Network.Security.UserDetail;
import com.example.Social_Network.User.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @RequestBody JWTAuthenticationRequest request, HttpServletResponse response
            )
    {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.getGeneratedToken(authentication);

        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        List<String> roles = userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        JWTResponse jwtResponse =
                JWTResponse.builder()
                        .token(jwt)
                        .id(userDetail.getId())
                        .email(userDetail.getEmail())
                        .roles(roles)
                        .build();
        response.addCookie(new Cookie("access_token", jwt));
        return ResponseEntity.ok(jwtResponse);
    }







}
