package org.poainternet.helpdeskapplication.commentsmodule.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDetailsResponse {
    private String commentId;
    private String issueId;
    private Commenter commenter;
    private String commentTime;
    private String lastEdited;
    private String commentTitle;
    private String commentType;
    private String commentString;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Commenter {
        private String profilePicture;
        private String username;
        private String firstName;
        private String otherName;
        private String userId;
    }
}
