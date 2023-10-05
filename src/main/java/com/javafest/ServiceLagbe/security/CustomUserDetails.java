package com.javafest.ServiceLagbe.security;

import com.javafest.ServiceLagbe.users.general.GeneralUser;
import com.javafest.ServiceLagbe.users.worker.WorkerUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private boolean isEnabled;
    private List<GrantedAuthority> authorities;

    public CustomUserDetails(GeneralUser user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.isEnabled = user.isEnable();
        this.authorities = Arrays.stream(user.getRole()
                .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public CustomUserDetails(WorkerUser user) {
        this.username = user.getNumber();
        this.password = user.getPin();
        this.isEnabled = user.isEnable();
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
        return username;
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
        return isEnabled;
    }
}
