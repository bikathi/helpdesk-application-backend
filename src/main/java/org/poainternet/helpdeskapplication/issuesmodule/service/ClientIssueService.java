package org.poainternet.helpdeskapplication.issuesmodule.service;

import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.issuesmodule.repository.ClientIssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientIssueService {
    @Autowired
    private ClientIssueRepository clientIssueRepository;
}
