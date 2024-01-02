package org.poainternet.helpdeskapplication.securitymodule.service;

import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.securitymodule.abstractions.GenericAccountsService;
import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.securitymodule.exception.EntityNotFoundException;
import org.poainternet.helpdeskapplication.securitymodule.exception.InternalServerError;
import org.poainternet.helpdeskapplication.securitymodule.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;
import java.util.Objects;

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

    @Override
    public UserAccount createUserAccount(UserAccount userAccount, MultipartFile profileImage) throws InternalServerError {
        String profileImageUrl = null;
        if(Objects.nonNull(profileImage)) {
            // TODO: upload profile image
        }

        userAccount.setProfileImage(profileImageUrl);

        if(userAccountRepository.existsByUsernameOrEmail(userAccount.getUsername(), userAccount.getEmail())) {
            throw new InternalServerError("Email or username already in use");
        }

        return userAccountRepository.save(userAccount);
    }
}
