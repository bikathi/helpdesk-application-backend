package org.poainternet.helpdeskapplication.securitymodule.repository;

import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends MongoRepository<UserAccount, String> {
    /**
     *
     * @param username is the unique username of the user tied to the account
     * @return Optional<T> that may be empty if the user does not exist
     */
    Optional<UserAccount> findByUsername(String username);
}
