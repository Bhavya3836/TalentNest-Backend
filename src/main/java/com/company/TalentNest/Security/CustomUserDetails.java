package com.company.TalentNest.Security;

import com.company.TalentNest.Model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Integer companyId;

    public CustomUserDetails(User user) {
        this.id = user.getId().longValue();
        this.email = user.getEmail();
        this.password = user.getPassword();
        // ✅ Fixed: Use role name directly without prefixing "ROLE_"
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
        this.companyId = user.getCompany() != null ? user.getCompany().getId() : null;
    }

    public Long getId() {
        return id;
    }

    public Integer getCompanyId() {
        return companyId;
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

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}