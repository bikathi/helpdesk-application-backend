package org.poainternet.helpdeskapplication.issuesmodule.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poainternet.helpdeskapplication.issuesmodule.definitions.Location;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
        private String otherName;
        private String profileImage;
    }
}
