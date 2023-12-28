package org.poainternet.helpdeskapplication.securitymodule.component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.securitymodule.definitions.UserDetailsImpl;
import org.poainternet.helpdeskapplication.securitymodule.exception.CustomAuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@Slf4j
@Component
public class JWTUtils {
    @Value("${application.security.jwt-secret}")
    private String jwtSecret;

    @Value("${application.security.jwt-expiration-ms}")
    private Long jwtExpirationMS;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
    }

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getUserid());
        claims.put("userRoles", userDetails.getAuthorities());

        return Jwts.builder()
            .subject(userDetails.getUsername())
            .claims(claims)
            .expiration(new Date((new Date()).getTime() + jwtExpirationMS))
            .issuedAt(new Date())
            .signWith(key(), Jwts.SIG.HS256)
        .compact();
    }

    public boolean validateToken(String authToken) throws CustomAuthenticationException {
        try {
            Jwts.parser().verifyWith(key()).build().parse(authToken);
            return true;
        } catch(Exception ex) {
            if(Objects.equals(ex.getClass(), MalformedJwtException.class)) {
                log.error("Invalid JSON web token: {}", ex.getMessage());
            } else if(Objects.equals(ex.getClass(), ExpiredJwtException.class)) {
                log.error("JSON web token is expired: {}", ex.getMessage());
            } else if(Objects.equals(ex.getClass(), UnsupportedJwtException.class)) {
                log.error("JSON web token is unsupported: {}", ex.getMessage());
            } else if(Objects.equals(ex.getClass(), IllegalArgumentException.class)) {
                log.error("JSON web token claims string is empty: {}", ex.getMessage());
            }

            throw new CustomAuthenticationException("Invalid authorization token");
        }
    }

    public String getUsernameFromToken(String authToken) {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(authToken).getPayload().getSubject();
    }
}
