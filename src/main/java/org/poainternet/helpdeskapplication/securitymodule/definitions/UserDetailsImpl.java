package org.poainternet.helpdeskapplication.securitymodule.definitions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private String userid;
    private String username;
    private String password;
    private Boolean accountEnabled;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(UserAccount userAccount) {
        this.userid = userAccount.getUserId();
        this.username = userAccount.getUsername();
        this.password = userAccount.getPassword();
        this.accountEnabled = userAccount.getAccountEnabled();
        this.authorities = userAccount.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
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
