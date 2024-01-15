package org.poainternet.helpdeskapplication.issuesmodule.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ClientIssueResponse {
    private String issueId;
    private String issueTitle;
    private UserEntity openedByUser;
    private UserEntity closedByUser;
    private String clientName;
    private Location clientLocation;
    private String dateReported;
    private String clientEmail;
    private String clientPhone;
    private String issueStatus;
    private Boolean issueClosed;
    private String issueDescription;
    private List<UserEntity> handlerUsers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserEntity {
        private String userId;
        private String username;
        private String firstName;
        private String lastName;
        private String profileImage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private String county;
        private String area;
    }
}
