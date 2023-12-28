package org.poainternet.helpdeskapplication.securitymodule.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.poainternet.helpdeskapplication.securitymodule.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

public class JWTValidationFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException, AuthenticationException {
        try {
            // extract the JWT and verify it
            String authToken = this.parseJwt(request);
            if(Objects.isNull(authToken)) {
                throw new AuthException("Invalid JWT token provided");
            }
        } catch() {
            
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");

        if(StringUtils.hasText(authToken) && authToken.startsWith("Bearer ")) {
            return authToken.substring(7);
        }

        return null;
    }
}
