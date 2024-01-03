package org.poainternet.helpdeskapplication.securitymodule;

import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.securitymodule.abstractions.GenericAccountsController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AccountsModuleController implements GenericAccountsController {
    private final String CLASS_NAME = this.getClass().getName();

    @Override
    public ResponseEntity<?> updateUserAccount() {
        return null;
    }

    @Override
    public ResponseEntity<?> deactivateUserAccount() {
        return null;
    }

    @Override
    public ResponseEntity<?> activateUserAccount() {
        return null;
    }

    @Override
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getAccountsAsPage() {
        return null;
    }
}
