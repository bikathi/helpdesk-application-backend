package org.poainternet.helpdeskapplication.securitymodule.repository;

import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     *
     * @param username is the username candidate we are searching for
     * @param email is the email candidate we are searching for
     * @return true if an account by that username or email exist
     */
    boolean existsByUsernameOrEmail(String username, String email);

    /**
     *
     * @param pageable defines the size and page number of the current request
     * @return a Page<UseAccount> of the size defined by the 'size' in Pageable
     */
    Page<UserAccount> findAll(Pageable pageable);
}
