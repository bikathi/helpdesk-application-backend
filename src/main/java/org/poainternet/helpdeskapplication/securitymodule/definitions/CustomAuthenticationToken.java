package org.poainternet.helpdeskapplication.securitymodule.definitions;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;
import java.util.UUID;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getLeastSignificantBits();
    private final Object principal;
    private final Object credentials;

    public CustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public CustomAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(false);
    }

    public static CustomAuthenticationToken authenticated (Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new CustomAuthenticationToken(principal, credentials, authorities);
    }

    public static CustomAuthenticationToken unauthenticated (Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new CustomAuthenticationToken(principal, credentials);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
