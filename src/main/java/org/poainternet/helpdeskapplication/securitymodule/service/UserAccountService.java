package org.poainternet.helpdeskapplication.securitymodule.service;

import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.securitymodule.abstractions.GenericAccountsService;
import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.securitymodule.exception.EntityNotFoundException;
import org.poainternet.helpdeskapplication.securitymodule.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Locale;

@Slf4j
@Repository
public class UserAccountService implements GenericAccountsService<UserAccount> {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserAccount getAccountByUsername(String username) throws EntityNotFoundException {
        return userAccountRepository.findByUsername(username).orElseThrow(
            () -> new EntityNotFoundException(String.format(Locale.ENGLISH, "Not found account for username %s", username)));
    }
}
