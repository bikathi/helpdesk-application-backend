package org.poainternet.helpdeskapplication.securitymodule;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.poainternet.helpdeskapplication.securitymodule.abstractions.GenericControllerHelper;
import org.poainternet.helpdeskapplication.securitymodule.abstractions.GenericAccountsController;
import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.sharedexceptions.InternalServerError;
import org.poainternet.helpdeskapplication.securitymodule.payload.request.ManAccRequest;
import org.poainternet.helpdeskapplication.securitymodule.payload.request.UpdatePasswordRequest;
import org.poainternet.helpdeskapplication.securitymodule.payload.response.AccDetailsResponse;
import org.poainternet.helpdeskapplication.securitymodule.payload.response.GenericResponse;
import org.poainternet.helpdeskapplication.securitymodule.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/accounts")
public class AccountsModuleController implements GenericAccountsController, GenericControllerHelper {
    private final String CLASS_NAME = this.getClass().getName();

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/signup")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> signup(@RequestBody ManAccRequest request) {
        UserAccount userAccount = UserAccount.builder()
            .firstName(request.getFirstName())
            .otherName(request.getOtherName())
            .password(passwordEncoder.encode("poaInternetDefault"))
            .username(request.getUsername())
            .email(request.getEmail())
            .department(request.getDepartment())
            .profileImage(request.getProfileURL())
            .accountEnabled(true)
            .dateOfBirth(this.dateStringToLocalDate(request.getDateOfBirth()))
            .roles(this.stringColToRoleEnumCol(request.getRoles()))
        .build();
        userAccount = userAccountService.createUserAccount(userAccount);
        AccDetailsResponse response = (AccDetailsResponse) this.convertEntityToPayload(userAccount, AccDetailsResponse.class);
        response.setRoles(this.roleEnumColToStringCol(userAccount.getRoles()));
        response.setDateOfBirth(this.localDateToDateString(userAccount.getDateOfBirth()));

        return ResponseEntity.ok().body(
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
    @PutMapping(value = "/update-account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateUserAccount(@RequestBody ManAccRequest request) {
        UserAccount existingAccount = userAccountService.getAccountById(request.getUserId());
        existingAccount.setFirstName(request.getFirstName());
        existingAccount.setOtherName(request.getOtherName());
        existingAccount.setUsername(request.getUsername());
        existingAccount.setEmail(request.getEmail());
        existingAccount.setDateOfBirth(this.dateStringToLocalDate(request.getDateOfBirth()));
        existingAccount.setRoles(this.stringColToRoleEnumCol(request.getRoles()));
        existingAccount.setDepartment(request.getDepartment());
        userAccountService.saveAccountDetails(existingAccount);

        return ResponseEntity.ok().body(
            new GenericResponse<>(
                apiVersion,
                organizationName,
                "Details updated successfully",
                HttpStatus.OK.value(),
                null
            )
        );
    }

    @Override
    @PatchMapping(value = "/update-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER') and #request.userId == authentication.principal.userid")
    public ResponseEntity<?> updateAccountPassword(@RequestBody UpdatePasswordRequest request) {
        if(!Objects.equals(request.getNewPassword(), request.getPasswordConfirmation())) {
            throw new InternalServerError("New passwords do not match");
        }

        UserAccount existingAccount = userAccountService.getAccountById(request.getUserId());
        if(!passwordEncoder.matches(request.getOldPassword(), existingAccount.getPassword())) {
            throw new InternalServerError("Old password is invalid");
        }

        existingAccount.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userAccountService.saveAccountDetails(existingAccount);
        log.info("Successfully updated password for user {}", request.getUserId());

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
    @PatchMapping(value = "/modify-roles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> modifyUserRoles(@RequestBody ManAccRequest request) {
        UserAccount existingAccount = userAccountService.getAccountById(request.getUserId());
        existingAccount.setRoles(this.stringColToRoleEnumCol(request.getRoles()));
        userAccountService.saveAccountDetails(existingAccount);
        return ResponseEntity.ok().body(
            new GenericResponse<>(
                apiVersion,
                organizationName,
                "Operation successful",
                HttpStatus.OK.value(),
                null
            )
        );
    }

    @Override
    @PatchMapping(value = "/deactivate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_USER')")
    public ResponseEntity<?> deactivateUserAccount(@RequestBody ManAccRequest request) {
        UserAccount existingAccount = userAccountService.getAccountById(request.getUserId());
        existingAccount.setAccountEnabled(false);
        userAccountService.saveAccountDetails(existingAccount);

        return ResponseEntity.ok().body(
            new GenericResponse<>(
                apiVersion,
                organizationName,
                "Operation successful",
                HttpStatus.OK.value(),
                null
            )
        );
    }

    @Override
    @PatchMapping(value = "/activate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> activateUserAccount(@RequestBody ManAccRequest request) {
        UserAccount existingAccount = userAccountService.getAccountById(request.getUserId());
        existingAccount.setAccountEnabled(true);
        userAccountService.saveAccountDetails(existingAccount);

        return ResponseEntity.ok().body(
            new GenericResponse<>(
                apiVersion,
                organizationName,
                "Operation successful",
                HttpStatus.OK.value(),
                null
            )
        );
    }

    @Override
    @GetMapping(value = "/get-list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAccountsAsPage(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<UserAccount> userAccounts = userAccountService.findListOfAccounts(page, size);
        List<AccDetailsResponse> responseList = userAccounts.parallelStream().map(
            account -> {
                AccDetailsResponse response = (AccDetailsResponse) this.convertEntityToPayload(account, AccDetailsResponse.class);
                response.setRoles(this.roleEnumColToStringCol(account.getRoles()));
                response.setDateOfBirth(this.localDateToDateString(account.getDateOfBirth()));
                return response;
            }).toList();
        log.info("{}: successfully retrieved page {} accounts list", CLASS_NAME, page);

        return ResponseEntity.ok().body(
            new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully retrieved paged accounts list",
                HttpStatus.OK.value(),
                responseList
            )
        );
    }

    @Override
    @GetMapping(value = "/get-account", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAccountById(@RequestParam(name = "userId") String userId) {
        log.info("request has reached controller...");
        UserAccount existingAccount = userAccountService.getAccountById(userId);
        AccDetailsResponse response = (AccDetailsResponse) this.convertEntityToPayload(existingAccount, AccDetailsResponse.class);
        response.setRoles(this.roleEnumColToStringCol(existingAccount.getRoles()));
        response.setDateOfBirth(this.localDateToDateString(existingAccount.getDateOfBirth()));

        log.info("{}: successfully retrieved account for userId {}", CLASS_NAME, userId);
        return ResponseEntity.ok().body(
            new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully retrieved user account",
                HttpStatus.OK.value(),
                response
            )
        );
    }

    @Override
    public Object convertEntityToPayload(UserAccount entity, Class<?> target) {
        return mapper.map(entity, target);
    }
}
