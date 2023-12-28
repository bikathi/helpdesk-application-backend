package org.poainternet.helpdeskapplication.securitymodule.definitions;

import lombok.Getter;
import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

@Getter
public class UserDetailsImpl implements UserDetails {
    private final String username;
    private final String password;
    private final Boolean accountEnabled;
    private final String fullName;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(UserAccount userAccount) {
        this.username = userAccount.getUsername();
        this.password = userAccount.getPassword();
        this.accountEnabled = userAccount.getAccountEnabled();
        this.fullName = String.format(Locale.ENGLISH, "%s %s", userAccount.getFirstName(), userAccount.getLastName());
        this.authorities = userAccount.getUserRoles().stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
        return this.accountEnabled;
    }
}
