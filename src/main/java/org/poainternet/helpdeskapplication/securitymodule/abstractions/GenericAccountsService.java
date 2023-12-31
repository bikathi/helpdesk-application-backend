package org.poainternet.helpdeskapplication.securitymodule.abstractions;

import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.sharedexceptions.EntityNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GenericAccountsService<T> {
    UserAccount getAccountByUsername(String username) throws EntityNotFoundException;
    UserAccount getAccountById(String id) throws EntityNotFoundException;
    UserAccount createUserAccount(UserAccount userAccount, MultipartFile profileImage);
    List<UserAccount> findListOfAccounts(Integer page, Integer size);
    void saveAccountDetails(UserAccount account);
}
