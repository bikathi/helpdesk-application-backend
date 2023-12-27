package org.poainternet.helpdeskapplication.securitymodule;

import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.securitymodule.abstractions.GenericAccountsController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AccountsModuleController implements GenericAccountsController {
    @Override
    public ResponseEntity<?> createUserAccount() {
        return null;
    }

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
}
