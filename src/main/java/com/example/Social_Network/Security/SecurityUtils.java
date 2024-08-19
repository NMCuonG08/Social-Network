package com.example.Social_Network.Security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class SecurityUtils {
    public UserDetail getUserDetails(Principal principal) {
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) principal;
        Object object = user.getPrincipal();
        return (UserDetail) object;
    }

    public UserDetail getUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (UserDetail) object;
    }
}
