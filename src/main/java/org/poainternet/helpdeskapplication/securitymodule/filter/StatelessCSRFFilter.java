package org.poainternet.helpdeskapplication.securitymodule.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.securitymodule.exception.CustomAuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class StatelessCSRFFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(new DefaultRequiresCsrfMatcher().matches(request)) {
            log.info("Running stateless CSRF filter...");
            final String csrfTokenValue = request.getHeader("X-CSRF-TOKEN");
            final Cookie[] cookies = request.getCookies();
            String csrfCookieValue = null;

            if(Objects.nonNull(cookies)) {
                for(Cookie cookie : cookies) {
                    if(Objects.equals(cookie.getName(), "CSRF-TOKEN")) {
                        csrfCookieValue = cookie.getValue();
                    }
                }
            }

            if(Objects.isNull(csrfTokenValue) || !Objects.equals(csrfTokenValue, csrfCookieValue)) {
                throw new CustomAuthenticationException("Missing required or non-matching CSRF token");
            }
        }

        filterChain.doFilter(request, response);
    }

    public static final class DefaultRequiresCsrfMatcher implements RequestMatcher {
        private final List<String> allowedHttpMethods = List.of("GET", "HEAD", "TRACE", "OPTIONS");
        private final List<String> allowedURIs = List.of("/api/v1/auth/signin", "/api/v1/auth/signup");

        @Override
        public boolean matches(HttpServletRequest request) {
            return !allowedHttpMethods.contains(request.getMethod()) && !allowedURIs.contains(request.getRequestURI());
        }
    }
}
