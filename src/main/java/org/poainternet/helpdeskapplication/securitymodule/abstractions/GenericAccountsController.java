package org.poainternet.helpdeskapplication.securitymodule.abstractions;

import org.poainternet.helpdeskapplication.securitymodule.payload.request.ModifyAccRequest;
import org.springframework.http.ResponseEntity;

public interface GenericAccountsController {
    ResponseEntity<?> updateUserAccount();
    ResponseEntity<?> deactivateUserAccount(ModifyAccRequest request);
    ResponseEntity<?> activateUserAccount(ModifyAccRequest request);
    ResponseEntity<?> getAccountsAsPage(Integer page, Integer size);
}
