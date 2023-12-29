package org.poainternet.helpdeskapplication.securitymodule;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.poainternet.helpdeskapplication.securitymodule.abstractions.EntityModelMapper;
import org.poainternet.helpdeskapplication.securitymodule.component.JWTUtils;
import org.poainternet.helpdeskapplication.securitymodule.definitions.CustomAuthenticationToken;
import org.poainternet.helpdeskapplication.securitymodule.definitions.UserDetailsImpl;
import org.poainternet.helpdeskapplication.securitymodule.definitions.UserRole;
import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.securitymodule.payload.request.SignInRequest;
import org.poainternet.helpdeskapplication.securitymodule.payload.response.GenericResponse;
import org.poainternet.helpdeskapplication.securitymodule.payload.response.SignInResponse;
import org.poainternet.helpdeskapplication.securitymodule.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;
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

    @PostMapping(value = "/signin",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest) {
        Authentication authentication = new CustomAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserAccount userAccount = userAccountService.getAccountByUsername(userDetails.getUsername());
        String authToken = jwtUtils.generateToken(authentication);
        Set<String> userRoles = new RoleConverter().roleEnumColToStringCol(userAccount.getRoles());

        SignInResponse response = (SignInResponse) this.convertEntityToPayload(userAccount, SignInResponse.class);
        response.setRoles(userRoles);
        response.setAuthToken(authToken);
        log.info("{}: successfully signed in user {}", CLASS_NAME, userDetails.getUsername());

        return ResponseEntity.ok().body(
           new GenericResponse<SignInResponse>(
               apiVersion,
               organizationName,
               "Login successful",
               HttpStatus.OK.value(),
               response
           )
        );
    }

    @PostMapping(value = "/signup")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> signup() {

    }

    @Override
    public Object convertEntityToPayload(UserAccount entity, Class<?> target) {
        return mapper.map(entity, target);
    }

    private final static class RoleConverter {
        private HashSet<String> roleEnumColToStringCol(Set<UserRole> roles) {
            HashSet<String> userRoles = new HashSet<>();
            roles.forEach(role -> {
                switch(role.name()) {
                    case "ROLE_USER" -> {
                        userRoles.add("role_user");
                    }

                    case "ROLE_MODERATOR" -> {
                        userRoles.add("role_moderator");
                    }

                    case "ROLE_ADMIN" -> {
                        userRoles.add("role_admin");
                    }

                    case "ROLE_MANAGER" -> {
                        userRoles.add("role_manager");
                    }
                }
            });

            return userRoles;
        }

        private Set<UserRole> stringColToRoleEnumCol(Collection<String> userRoles) {
            return null;
        }
    }
}
