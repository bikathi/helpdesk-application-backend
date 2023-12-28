package org.poainternet.helpdeskapplication.securitymodule.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.poainternet.helpdeskapplication.securitymodule.exception.CustomAuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class StatelessCSRFFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(new DefaultRequiresCsrfMatcher().matches(request)) {
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
                throw new CustomAuthenticationException("Missing or non-matching CSRF token");
            }
        }

        filterChain.doFilter(request, response);
    }

    public static final class DefaultRequiresCsrfMatcher implements RequestMatcher {
        private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPIONS)$");

        @Override
        public boolean matches(HttpServletRequest request) {
            return !allowedMethods.matcher(request.getMethod()).matches();
        }
    }
}
