package org.poainternet.helpdeskapplication.issuesmodule.service;

import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.issuesmodule.abstractions.GenericClientIssueService;
import org.poainternet.helpdeskapplication.issuesmodule.entity.ClientIssue;
import org.poainternet.helpdeskapplication.issuesmodule.repository.ClientIssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ClientIssueService implements GenericClientIssueService {
    @Autowired
    private ClientIssueRepository clientIssueRepository;

    @Override
    public ClientIssue createNewIssue(ClientIssue clientIssue) {
        return clientIssueRepository.save(clientIssue);
    }

    @Override
    public ClientIssue getClientIssueById(String issueId) {
        return null;
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
}
