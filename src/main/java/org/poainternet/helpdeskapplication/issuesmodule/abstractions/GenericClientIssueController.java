package org.poainternet.helpdeskapplication.issuesmodule.abstractions;

import org.springframework.http.ResponseEntity;

public interface GenericClientIssueController {
    ResponseEntity<?> openNewIssue();
    ResponseEntity<?> markIssueAsClosed();

    ResponseEntity<?> searchIssues();

    ResponseEntity<?> getListOfIssues();

    ResponseEntity<?> getIssueById();
}
