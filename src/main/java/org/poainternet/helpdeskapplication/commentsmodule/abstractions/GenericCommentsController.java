package org.poainternet.helpdeskapplication.commentsmodule.abstractions;

import org.poainternet.helpdeskapplication.commentsmodule.payload.request.AddCommentRequest;
import org.poainternet.helpdeskapplication.commentsmodule.payload.request.UpdateCommentRequest;
import org.springframework.http.ResponseEntity;

public interface GenericCommentsController {
    ResponseEntity<?> addNewComment(AddCommentRequest request);
    ResponseEntity<?> updateComment(UpdateCommentRequest request);
    ResponseEntity<?> getCommentList(String issueId, Integer page);
}
