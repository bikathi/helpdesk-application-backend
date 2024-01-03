package org.poainternet.helpdeskapplication.securitymodule.abstractions;

import org.poainternet.helpdeskapplication.securitymodule.payload.request.ModifyAccRequest;
import org.springframework.http.ResponseEntity;

public interface GenericAccountsController {
    ResponseEntity<?> updateUserAccount(ModifyAccRequest request);

    ResponseEntity<?> modifyUserRoles(ModifyAccRequest request);
    ResponseEntity<?> deactivateUserAccount(ModifyAccRequest request);
    ResponseEntity<?> activateUserAccount(ModifyAccRequest request);
    ResponseEntity<?> getAccountsAsPage(Integer page, Integer size);
}
