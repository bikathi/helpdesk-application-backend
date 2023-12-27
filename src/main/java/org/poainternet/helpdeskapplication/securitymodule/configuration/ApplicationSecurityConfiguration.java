package org.poainternet.helpdeskapplication.securitymodule.configuration;

import org.poainternet.helpdeskapplication.securitymodule.exception.AuthEntryPointJWT;
import org.poainternet.helpdeskapplication.securitymodule.filter.JWTValidationFilter;
import org.poainternet.helpdeskapplication.securitymodule.filter.StatelessCSRFFilter;
import org.poainternet.helpdeskapplication.securitymodule.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
@EnableMethodSecurity
public class ApplicationSecurityConfiguration {
    @Autowired
    private AuthEntryPointJWT securityExceptionHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public JWTValidationFilter jwtValidationFilter() {
        return new JWTValidationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(securityCsrfConfigurer -> securityCsrfConfigurer.disable().addFilterBefore(new StatelessCSRFFilter(), CsrfFilter.class))
            .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(securityExceptionHandler))
            .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.authenticationProvider(daoAuthenticationProvider());
        httpSecurity.addFilterBefore(jwtValidationFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}