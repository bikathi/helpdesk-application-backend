package org.poainternet.helpdeskapplication.issuesmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModIssueRequest {
    private String issueId;
    private String closedByUserId;
    private String openedByUserId;
}
