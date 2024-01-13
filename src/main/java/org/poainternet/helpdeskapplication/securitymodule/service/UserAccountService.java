package org.poainternet.helpdeskapplication.securitymodule.service;

import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.securitymodule.abstractions.GenericAccountsService;
import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.securitymodule.definitions.SearchCriteriaDefinition;
import org.poainternet.helpdeskapplication.securitymodule.util.ModuleUtil;
import org.poainternet.helpdeskapplication.sharedexceptions.EntityNotFoundException;
import org.poainternet.helpdeskapplication.sharedexceptions.InternalServerError;
import org.poainternet.helpdeskapplication.securitymodule.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class UserAccountService implements GenericAccountsService<UserAccount> {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public UserAccount getAccountByUsername(String username) throws EntityNotFoundException {
        return userAccountRepository.findByUsername(username).orElseThrow(
            () -> new EntityNotFoundException(String.format(Locale.ENGLISH, "Not found account for username %s", username)));
    }

    @Override
    public UserAccount getAccountById(String id) throws EntityNotFoundException {
        return userAccountRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format(Locale.ENGLISH, "Not found account for id %s", id)));
    }

    @Override
    public UserAccount createUserAccount(UserAccount userAccount) throws InternalServerError {
        if(userAccountRepository.existsByUsernameOrEmail(userAccount.getUsername(), userAccount.getEmail())) {
            throw new InternalServerError("Email or username already in use");
        }

        return userAccountRepository.save(userAccount);
    }

    @Override
    public List<UserAccount> findListOfAccounts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return userAccountRepository.findAll(pageable).stream().peek(account -> account.setPassword(null)).collect(Collectors.toList());
    }

    @Override
    public void saveAccountDetails(UserAccount account) {
        userAccountRepository.save(account);
    }

    @Override
    public List<UserAccount> searchAccountsByCriteria(SearchCriteriaDefinition searchCriteria) {
        Query searchQuery =  ModuleUtil.generateSearchQuery(searchCriteria);
        return mongoTemplate.find(searchQuery, UserAccount.class);
    }
}
