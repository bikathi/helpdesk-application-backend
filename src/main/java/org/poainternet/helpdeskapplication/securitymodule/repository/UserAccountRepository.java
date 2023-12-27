package org.poainternet.helpdeskapplication.securitymodule.repository;

import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends MongoRepository<UserAccount, String> {
}
