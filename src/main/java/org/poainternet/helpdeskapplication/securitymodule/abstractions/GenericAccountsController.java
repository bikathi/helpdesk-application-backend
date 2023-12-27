package org.poainternet.helpdeskapplication.securitymodule.abstractions;

import org.springframework.http.ResponseEntity;

public interface GenericAccountsController {
    ResponseEntity<?> createUserAccount();
    ResponseEntity<?> updateUserAccount();
    ResponseEntity<?> deactivateUserAccount();
    ResponseEntity<?> activateUserAccount();
}
