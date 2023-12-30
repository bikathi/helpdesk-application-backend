package org.poainternet.helpdeskapplication.securitymodule.configuration;

import org.poainternet.helpdeskapplication.securitymodule.component.AccessDeniedJWTHandler;
import org.poainternet.helpdeskapplication.securitymodule.component.AuthEntryPointJWT;
import org.poainternet.helpdeskapplication.securitymodule.filter.JWTValidationFilter;
import org.poainternet.helpdeskapplication.securitymodule.filter.StatelessCSRFFilter;
import org.poainternet.helpdeskapplication.securitymodule.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    jsr250Enabled = true,
    securedEnabled = true)
public class ApplicationSecurityConfiguration {
    @Autowired
    private AuthEntryPointJWT authorizationExceptionHandler;

    @Autowired
    private AccessDeniedJWTHandler accessDeniedExceptionHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public JWTValidationFilter jwtValidationFilter() {
        return new JWTValidationFilter();
    }

    @Bean
    public StatelessCSRFFilter statelessCSRFFilter() {
        return new StatelessCSRFFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain springSecurity(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .exceptionHandling(exceptionHandlingConfigurer -> {
                exceptionHandlingConfigurer.authenticationEntryPoint(authorizationExceptionHandler);
                exceptionHandlingConfigurer.accessDeniedHandler(accessDeniedExceptionHandler);
            })
            .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(requestMatcherRegistry ->
                requestMatcherRegistry.requestMatchers("/api/v1/auth/**").permitAll())
            .authenticationProvider(daoAuthenticationProvider())
            .addFilterBefore(jwtValidationFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(statelessCSRFFilter(), CsrfFilter.class);

        return httpSecurity.build();
    }
}
