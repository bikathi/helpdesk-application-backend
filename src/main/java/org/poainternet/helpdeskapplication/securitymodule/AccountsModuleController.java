package org.poainternet.helpdeskapplication.securitymodule;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.poainternet.helpdeskapplication.securitymodule.abstractions.GenericControllerHelper;
import org.poainternet.helpdeskapplication.securitymodule.abstractions.GenericAccountsController;
import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.securitymodule.payload.request.ModifyAccRequest;
import org.poainternet.helpdeskapplication.securitymodule.payload.response.AccDetailsResponse;
import org.poainternet.helpdeskapplication.securitymodule.payload.response.GenericResponse;
import org.poainternet.helpdeskapplication.securitymodule.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public ResponseEntity<?> updateUserAccount(@RequestBody ModifyAccRequest request) {
        return null;
    }

    @Override
    @PostMapping(value = "/modify-roles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modifyUserRoles(@RequestBody ModifyAccRequest request) {
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
    @PostMapping(value = "/deactivate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deactivateUserAccount(@RequestBody ModifyAccRequest request) {
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
    @PostMapping(value = "/activate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> activateUserAccount(@RequestBody ModifyAccRequest request) {
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
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getAccountsAsPage(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
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
    public Object convertEntityToPayload(UserAccount entity, Class<?> target) {
        return mapper.map(entity, target);
    }
}
