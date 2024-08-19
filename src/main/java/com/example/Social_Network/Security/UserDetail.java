package com.example.Social_Network.Security;

import com.example.Social_Network.User.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@Setter
@Getter
public class UserDetail implements UserDetails {
    private UUID id;
    private String email;
    private String password;
    private boolean isEnable;
    private List<GrantedAuthority>authorities;

    public UserDetail(User user) {
        this.email = user.getEmail();
        this.password = user.getPassWord();
        this.isEnable = user.isEnable();
        this.authorities = Arrays.stream(user.getRole()
                .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }
}
