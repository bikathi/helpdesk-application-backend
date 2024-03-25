package org.poainternet.helpdeskapplication.issuesmodule.service;

import com.africastalking.AfricasTalking;
import com.africastalking.SmsService;
import com.africastalking.sms.Recipient;
import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.issuesmodule.abstractions.GenericClientIssueService;
import org.poainternet.helpdeskapplication.issuesmodule.definitions.IssuesSearchCriteria;
import org.poainternet.helpdeskapplication.issuesmodule.entity.ClientIssue;
import org.poainternet.helpdeskapplication.issuesmodule.repository.ClientIssueRepository;
import org.poainternet.helpdeskapplication.issuesmodule.util.ModuleUtil;
import org.poainternet.helpdeskapplication.sharedexceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Value("${africastalking.application-name}")
    private String atAppName;
    @Value("${africastalking.api-key}")
    private String atApiKey;

    @Autowired
    private ClientIssueRepository clientIssueRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void createNewIssue(ClientIssue clientIssue) {
        ClientIssue issue = clientIssueRepository.save(clientIssue);
        this.issueOpenSendClientSMS(issue.getClientName(), issue.getClientPhone(), issue.getIssueId());
    }

    @Override
    public ClientIssue getClientIssueById(String issueId) {
        return clientIssueRepository.findById(issueId).orElseThrow(
            () -> new EntityNotFoundException(String.format(Locale.ENGLISH, "Not found account for id %s", issueId)));
    }

    @Override
    public List<ClientIssue> getListOfClientIssues(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return clientIssueRepository.findAll(pageable).toList();
    }

    @Override
    public List<ClientIssue> getListByClosedStatus(Integer page, Integer size, Boolean issueOpen) {
        Pageable pageable = PageRequest.of(page, size);
        return clientIssueRepository.findAllByIssueClosed(pageable, issueOpen).toList();
    }

    @Override
    public List<ClientIssue> searchClientIssues(IssuesSearchCriteria searchCriteria) {
        Query searchQuery = ModuleUtil.generateSearchQuery(searchCriteria);
        return mongoTemplate.find(searchQuery, ClientIssue.class);
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

        // send SMS update to client that the issue has been closed
        ClientIssue existingClientIssue = this.getClientIssueById(clientIssueId);
        this.issueClosedSendClientSMS(existingClientIssue.getClientName(), existingClientIssue.getClientPhone(), existingClientIssue.getIssueId());
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
            new Update().set("issueClosed", false).set("openedByUserId", openedByUserId).set("closedByUserId", "");
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);

        mongoTemplate.findAndModify(query, updateDefinition, options, ClientIssue.class);
    }

    public void issueOpenSendClientSMS(String clientName, String clientPhone, String openedIssueId) {
        final String CLIENT_MESSAGE = "Hello " + clientName +
                "! an issue has been opened for you at Poa Internet with ID " + openedIssueId +
                ". Feel free to contact customer care and provide this id in case of any issue you may have.";

        AfricasTalking.initialize(atAppName, atApiKey);
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        try {
            List<Recipient> response = sms.send(CLIENT_MESSAGE, new String[] { clientPhone }, true);
            log.info("SMS response: {}", response);
        } catch(Exception ex) {
            log.error("SMS exception occurred: {}", ex.getMessage());
        }
    }

    public void issueClosedSendClientSMS(String clientName, String clientPhone, String openedIssueId) {
        final String CLIENT_MESSAGE = "Hello " + clientName +
                "! Please note that your issue with ID " + openedIssueId +
                ". has been closed. Should the issue arise again, feel free to contact customer care and provide this ID again.";

        AfricasTalking.initialize(atAppName, atApiKey);
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        try {
            List<Recipient> response = sms.send(CLIENT_MESSAGE, new String[] { clientPhone }, true);
            log.info("SMS response: {}", response);
        } catch(Exception ex) {
            log.error("SMS exception occurred: {}", ex.getMessage());
        }
    }
}
