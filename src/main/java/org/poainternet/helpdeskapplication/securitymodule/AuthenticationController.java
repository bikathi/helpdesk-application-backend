package org.poainternet.helpdeskapplication.securitymodule;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.poainternet.helpdeskapplication.securitymodule.abstractions.GenericControllerHelper;
import org.poainternet.helpdeskapplication.securitymodule.component.JWTUtils;
import org.poainternet.helpdeskapplication.securitymodule.definitions.UserDetailsImpl;
import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.sharedconfigs.CorsConfiguration;
import org.poainternet.helpdeskapplication.sharedexceptions.InternalServerError;
import org.poainternet.helpdeskapplication.securitymodule.payload.request.SignInRequest;
import org.poainternet.helpdeskapplication.securitymodule.payload.request.UpdatePasswordRequest;
import org.poainternet.helpdeskapplication.securitymodule.payload.response.AccDetailsResponse;
import org.poainternet.helpdeskapplication.securitymodule.payload.response.GenericResponse;
import org.poainternet.helpdeskapplication.securitymodule.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth")
@CorsConfiguration
public class AuthenticationController implements GenericControllerHelper {
    private final String CLASS_NAME = this.getClass().getName();

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @Value("${application.security.xsrf-secret}")
    private String xsrfSecret;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/signin",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        UserAccount userAccount = userAccountService.getAccountByUsername(userDetails.getUsername());
        String authToken = jwtUtils.generateToken(authentication);
        Set<String> userRoles = this.roleEnumColToStringCol(userAccount.getRoles());

        AccDetailsResponse response = (AccDetailsResponse) this.convertEntityToPayload(userAccount, AccDetailsResponse.class);
        response.setRoles(userRoles);
        response.setAuthToken(authToken);
        response.setCsrfToken(xsrfSecret);
        response.setDateOfBirth(this.localDateToDateString(userAccount.getDateOfBirth()));
        log.info("{}: successfully signed in user {}", CLASS_NAME, userAccount.getUsername());

        return ResponseEntity.ok().body(
            new GenericResponse<>(
                apiVersion,
                organizationName,
                "Login successful",
                HttpStatus.OK.value(),
                response
            )
        );
    }

    @PatchMapping(value = "/password-reset")
    public ResponseEntity<?> updateAccountPassword(@RequestBody UpdatePasswordRequest request) {
        if(!Objects.equals(request.getNewPassword(), request.getPasswordConfirmation())) {
            throw new InternalServerError("New passwords do not match");
        }

        UserAccount existingAccount = userAccountService.getAccountById(request.getUserId());
        existingAccount.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userAccountService.saveAccountDetails(existingAccount);

        return ResponseEntity.ok().body(
            new GenericResponse<>(
                apiVersion,
                organizationName,
                "Password updated successfully",
                HttpStatus.OK.value(),
                null
            )
        );
    }

    @Override
    public Object convertEntityToPayload(UserAccount entity, Class<?> target) {
        return mapper.map(entity, target);
    }
}
