package org.poainternet.helpdeskapplication.commentsmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentRequest {
    private String issueId;
    private String commentType;
    private String commenterId;
    private String commentTime;
    private String commentTitle;
    private String comment;
}
