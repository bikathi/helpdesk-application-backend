package org.poainternet.helpdeskapplication.issuesmodule;

import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.issuesmodule.abstractions.GenericClientIssueController;
import org.poainternet.helpdeskapplication.issuesmodule.service.ClientIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/issues")
public class IssueModuleController implements GenericClientIssueController {
    private final String CLASS_NAME = this.getClass().getName();

    @Autowired
    private ClientIssueService clientIssueService;

    @Override
    public ResponseEntity<?> openNewIssue() {
        return null;
    }

    @Override
    public ResponseEntity<?> markIssueAsClosed() {
        return null;
    }

    @Override
    public ResponseEntity<?> searchIssues() {
        return null;
    }

    @Override
    public ResponseEntity<?> getListOfIssues() {
        return null;
    }

    @Override
    public ResponseEntity<?> getIssueById() {
        return null;
    }
}
