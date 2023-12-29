package org.poainternet.helpdeskapplication.securitymodule.abstractions;

import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.securitymodule.exception.EntityNotFoundException;
import org.springframework.web.multipart.MultipartFile;

public interface GenericAccountsService<T> {
    UserAccount getAccountByUsername(String username) throws EntityNotFoundException;
    UserAccount createUserAccount(UserAccount userAccount, MultipartFile profileImage);
}
