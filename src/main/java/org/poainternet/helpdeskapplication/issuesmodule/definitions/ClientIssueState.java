package org.poainternet.helpdeskapplication.issuesmodule.definitions;

public enum ClientIssueState {
    ACTIVE("active"),
    INACTIVE("inactive"),
    DORMANT("dormant");

    private final String label;

    private ClientIssueState(String label) {
        this.label = label;
    }
}
