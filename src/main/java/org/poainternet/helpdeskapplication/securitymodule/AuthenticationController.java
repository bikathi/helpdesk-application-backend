package org.poainternet.helpdeskapplication.securitymodule;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.poainternet.helpdeskapplication.securitymodule.abstractions.EntityModelMapper;
import org.poainternet.helpdeskapplication.securitymodule.component.JWTUtils;
import org.poainternet.helpdeskapplication.securitymodule.definitions.UserDetailsImpl;
import org.poainternet.helpdeskapplication.securitymodule.definitions.UserRole;
import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.securitymodule.exception.InternalServerError;
import org.poainternet.helpdeskapplication.securitymodule.payload.request.SignInRequest;
import org.poainternet.helpdeskapplication.securitymodule.payload.response.GenericResponse;
import org.poainternet.helpdeskapplication.securitymodule.payload.response.AccDetailsResponse;
import org.poainternet.helpdeskapplication.securitymodule.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController implements EntityModelMapper {
    private final String CLASS_NAME = this.getClass().getName();

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

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
        Set<String> userRoles = new ControllerUtil().roleEnumColToStringCol(userAccount.getRoles());

        AccDetailsResponse response = (AccDetailsResponse) this.convertEntityToPayload(userAccount, AccDetailsResponse.class);
        response.setRoles(userRoles);
        response.setAuthToken(authToken);
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

    @PostMapping(value = "/signup")
//    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> signup(
        @RequestParam(name = "profile-picture", required = false) MultipartFile profilePicture,
        @RequestParam(name = "first-name") String firstName,
        @RequestParam(name = "other-name") String otherName,
        @RequestParam(name = "email-address") String emailAddress,
        @RequestParam(name = "date-of-birth") String dateOfBirth,
        @RequestParam(name = "user-roles") String userRoles,
        @RequestParam(name = "department") String department) {
        // convert string to array
        String[] assignedRoles = userRoles.split(", ");

        UserAccount userAccount = UserAccount.builder()
            .firstName(firstName)
            .otherName(otherName)
            .password(passwordEncoder.encode("poaInternetDefault"))
            .username(String.format("@%s%s", firstName.toLowerCase(), otherName.toLowerCase()))
            .email(emailAddress)
            .department(department)
            .accountEnabled(true)
            .dateOfBirth(new ControllerUtil().dateStringToLocalDate(dateOfBirth))
            .roles(new ControllerUtil().stringColToRoleEnumCol(assignedRoles))
        .build();
        userAccount = userAccountService.createUserAccount(userAccount, profilePicture);
        AccDetailsResponse response = (AccDetailsResponse) this.convertEntityToPayload(userAccount, AccDetailsResponse.class);
        response.setRoles(new ControllerUtil().roleEnumColToStringCol(userAccount.getRoles()));
        response.setDateOfBirth(new ControllerUtil().localDateToDateString(userAccount.getDateOfBirth()));

        return ResponseEntity.created(URI.create("")).body(
            new GenericResponse<>(
                apiVersion,
                organizationName,
                "Account created successfully",
                HttpStatus.CREATED.value(),
                response
            )
        );
    }

    @Override
    public Object convertEntityToPayload(UserAccount entity, Class<?> target) {
        return mapper.map(entity, target);
    }

    private final static class ControllerUtil {
        private HashSet<String> roleEnumColToStringCol(Set<UserRole> roles) {
            HashSet<String> userRoles = new HashSet<>();
            roles.forEach(role -> {
                switch(role.name()) {
                    case "ROLE_USER" -> userRoles.add("role_user");
                    case "ROLE_MODERATOR" -> userRoles.add("role_moderator");
                    case "ROLE_ADMIN" -> userRoles.add("role_admin");
                    case "ROLE_MANAGER" -> userRoles.add("role_manager");
                }
            });

            return userRoles;
        }

        private Set<UserRole> stringColToRoleEnumCol(String[] userRoles) throws InternalServerError {
            Set<UserRole> roles = new HashSet<>();
            for(String role : userRoles) {
                switch(role) {
                    case "role_user" -> roles.add(UserRole.ROLE_USER);
                    case "role_moderator" -> roles.add(UserRole.ROLE_MODERATOR);
                    case "role_admin" -> roles.add(UserRole.ROLE_ADMIN);
                    case "role_manager" -> roles.add(UserRole.ROLE_MANAGER);
                    default -> throw new InternalServerError(String.format("Undefined role %s", role));
                }
            }
            return roles;
        }

        private LocalDate dateStringToLocalDate(String date) {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH));
        }

        private String localDateToDateString(LocalDate date) {
            return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH));
        }
    }
}
