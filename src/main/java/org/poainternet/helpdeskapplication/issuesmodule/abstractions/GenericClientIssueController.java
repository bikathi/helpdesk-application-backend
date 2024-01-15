package org.poainternet.helpdeskapplication.issuesmodule.abstractions;

import org.poainternet.helpdeskapplication.issuesmodule.payload.request.CreateClientIssueRequest;
import org.springframework.http.ResponseEntity;

public interface GenericClientIssueController {
    ResponseEntity<?> openNewIssue(CreateClientIssueRequest request);
    ResponseEntity<?> markIssueAsClosed();

    ResponseEntity<?> searchIssues();

    ResponseEntity<?> getListOfIssues();

    ResponseEntity<?> getIssueById();
}
