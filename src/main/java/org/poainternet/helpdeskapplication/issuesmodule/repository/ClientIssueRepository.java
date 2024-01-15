package org.poainternet.helpdeskapplication.issuesmodule.repository;

import org.poainternet.helpdeskapplication.issuesmodule.entity.ClientIssue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientIssueRepository extends MongoRepository<ClientIssue, String> {
}
