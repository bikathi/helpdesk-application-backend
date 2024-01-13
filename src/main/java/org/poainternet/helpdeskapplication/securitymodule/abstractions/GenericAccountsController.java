package org.poainternet.helpdeskapplication.securitymodule.abstractions;

import org.poainternet.helpdeskapplication.securitymodule.payload.request.ManAccRequest;
import org.poainternet.helpdeskapplication.securitymodule.payload.request.UpdatePasswordRequest;
import org.springframework.http.ResponseEntity;

public interface GenericAccountsController {
    ResponseEntity<?> updateUserAccount(ManAccRequest request);
    ResponseEntity<?> updateAccountPassword(UpdatePasswordRequest request);
    ResponseEntity<?> modifyUserRoles(ManAccRequest request);
    ResponseEntity<?> deactivateUserAccount(ManAccRequest request);
    ResponseEntity<?> activateUserAccount(ManAccRequest request);
    ResponseEntity<?> getAccountsAsPage(Integer page, Integer size);
    ResponseEntity<?> getAccountById(String userId);
    ResponseEntity<?> searchAccountByCriteria(
        Integer page,
        String searchTerm,
        Boolean byId,
        Boolean byUsername,
        Boolean byFirstOrOtherName);
}
