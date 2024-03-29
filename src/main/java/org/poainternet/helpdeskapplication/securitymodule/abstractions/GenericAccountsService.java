package org.poainternet.helpdeskapplication.securitymodule.abstractions;

import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.securitymodule.definitions.AccountsSearchCriteria;
import org.poainternet.helpdeskapplication.sharedexceptions.EntityNotFoundException;

import java.util.List;

public interface GenericAccountsService<T> {
    UserAccount getAccountByUsername(String username) throws EntityNotFoundException;
    UserAccount getAccountById(String id) throws EntityNotFoundException;
    UserAccount createUserAccount(UserAccount userAccount);
    List<UserAccount> findListOfAccounts(Integer page, Integer size);
    void saveAccountDetails(UserAccount account);
    List<UserAccount> searchAccountsByCriteria(AccountsSearchCriteria searchCriteria);
}
