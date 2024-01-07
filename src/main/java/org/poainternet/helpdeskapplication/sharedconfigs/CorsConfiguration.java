package org.poainternet.helpdeskapplication.sharedconfigs;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@CrossOrigin(
    maxAge = 1800,
    origins = { "http://localhost:3000", "http://127.0.0.1:3000" },
    allowedHeaders = { "Authorization", "X-CSRF-TOKEN", "Content-Type", "User-Agent" })
public @interface CorsConfiguration {}
