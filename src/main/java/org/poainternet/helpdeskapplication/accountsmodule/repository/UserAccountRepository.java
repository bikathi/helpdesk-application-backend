package org.poainternet.helpdeskapplication.accountsmodule.repository;

import org.poainternet.helpdeskapplication.accountsmodule.entity.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends MongoRepository<UserAccount, String> {
}
