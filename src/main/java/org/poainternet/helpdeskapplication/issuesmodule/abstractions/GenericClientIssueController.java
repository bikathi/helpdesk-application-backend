package org.poainternet.helpdeskapplication.issuesmodule.abstractions;

import org.poainternet.helpdeskapplication.issuesmodule.payload.request.CreateClientIssueRequest;
import org.poainternet.helpdeskapplication.issuesmodule.payload.request.ModIssueRequest;
import org.springframework.http.ResponseEntity;

public interface GenericClientIssueController {
    ResponseEntity<?> openNewIssue(CreateClientIssueRequest request);
    ResponseEntity<?> markIssueAsClosed(ModIssueRequest request);

    ResponseEntity<?> reOpenIssue(ModIssueRequest request);

    ResponseEntity<?> searchIssues(
        Integer page,
        String searchQuery,
        Boolean byId,
        Boolean byClientEmail,
        Boolean byClientPhone,
        Boolean byHandlerUID
    );

    ResponseEntity<?> getListOfIssues(Integer page, Integer size);

    ResponseEntity<?> getIssueById(String clientIssueId);
}
