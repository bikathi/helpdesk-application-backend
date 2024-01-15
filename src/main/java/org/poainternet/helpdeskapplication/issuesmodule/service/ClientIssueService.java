package org.poainternet.helpdeskapplication.issuesmodule.service;

import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.issuesmodule.abstractions.GenericClientIssueService;
import org.poainternet.helpdeskapplication.issuesmodule.entity.ClientIssue;
import org.poainternet.helpdeskapplication.issuesmodule.repository.ClientIssueRepository;
import org.poainternet.helpdeskapplication.sharedexceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class ClientIssueService implements GenericClientIssueService {
    @Autowired
    private ClientIssueRepository clientIssueRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void createNewIssue(ClientIssue clientIssue) {
        clientIssueRepository.save(clientIssue);
    }

    @Override
    public ClientIssue getClientIssueById(String issueId) {
        return clientIssueRepository.findById(issueId).orElseThrow(
            () -> new EntityNotFoundException(String.format(Locale.ENGLISH, "Not found account for id %s", issueId)));
    }

    @Override
    public ClientIssue updateClientIssueStatus(String issueId, String status) {
        return null;
    }

    @Override
    public List<ClientIssue> getListOfClientIssues(Integer page, Integer size) {
        return null;
    }

    @Override
    public List<ClientIssue> searchClientIssues() {
        return null;
    }

    @Override
    public void changeIssueClosedStatusTrue(String clientIssueId, String closedByUserId) {
        Query query = new Query().addCriteria(Criteria.where("issueId").is(clientIssueId));
        /**
         * To the future generations reading this code:
         * When an issue's statusClosed is marked as true, we need to provide who closed the issue(closedByUserId).
         * However, DO NOT NULLIFY the openedByUserId field because it is required in the client side as metadata
         * to display who was the last person to open/ re-open the issue
         */
        UpdateDefinition updateDefinition = new Update().set("issueClosed", true).set("closedByUserId", closedByUserId);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);

        mongoTemplate.findAndModify(query, updateDefinition, options, ClientIssue.class);
    }

    @Override
    public void changeIssueClosedStatusFalse(String clientIssueId, String openedByUserId) {
        Query query = new Query().addCriteria(Criteria.where("issueId").is(clientIssueId));
        /**
         * To the future generations reading this code:
         * When an issue's statusClosed is marked as false(i.e is re-opened), we need to provide who opened the issue(openedByUserId).
         * In addition to that, we have to set the closedByUserId to NULL since the issue is now open and this field becomes
         * useless
         */
        UpdateDefinition updateDefinition =
            new Update().set("issueClosed", false).set("openedByUserId", openedByUserId).set("closedByUserId", null);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);

        mongoTemplate.findAndModify(query, updateDefinition, options, ClientIssue.class);
    }
}
