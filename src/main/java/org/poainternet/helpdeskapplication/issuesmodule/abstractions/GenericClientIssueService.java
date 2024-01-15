package org.poainternet.helpdeskapplication.issuesmodule.abstractions;

import org.poainternet.helpdeskapplication.issuesmodule.entity.ClientIssue;

import java.util.List;

public interface GenericClientIssueService {
    void createNewIssue(ClientIssue clientIssue);
    ClientIssue getClientIssueById(String issueId);
    ClientIssue updateClientIssueStatus(String issueId, String status);
    List<ClientIssue> getListOfClientIssues(Integer page, Integer size);
    List<ClientIssue> searchClientIssues();
    void changeIssueClosedStatusTrue(String clientIssueId, String closedByUserId);
    void changeIssueClosedStatusFalse(String clientIssueId, String openedByUserId);
}
