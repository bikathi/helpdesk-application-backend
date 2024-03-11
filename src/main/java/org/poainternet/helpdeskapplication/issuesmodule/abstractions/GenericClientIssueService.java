package org.poainternet.helpdeskapplication.issuesmodule.abstractions;

import org.poainternet.helpdeskapplication.issuesmodule.definitions.IssuesSearchCriteria;
import org.poainternet.helpdeskapplication.issuesmodule.entity.ClientIssue;

import java.util.List;

public interface GenericClientIssueService {
    void createNewIssue(ClientIssue clientIssue);
    ClientIssue getClientIssueById(String issueId);
    List<ClientIssue> getListOfClientIssues(Integer page, Integer size);
    List<ClientIssue> getListByClosedStatus(Integer page, Integer size, Boolean issueOpen);
    List<ClientIssue> searchClientIssues(IssuesSearchCriteria searchCriteria);
    void changeIssueClosedStatusTrue(String clientIssueId, String closedByUserId);
    void changeIssueClosedStatusFalse(String clientIssueId, String openedByUserId);
}
