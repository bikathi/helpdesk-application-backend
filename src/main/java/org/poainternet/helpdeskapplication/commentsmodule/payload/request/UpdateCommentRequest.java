package org.poainternet.helpdeskapplication.commentsmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequest {
    private String commentId;
    private String lastEdited;
    private String updatedComment;
}
