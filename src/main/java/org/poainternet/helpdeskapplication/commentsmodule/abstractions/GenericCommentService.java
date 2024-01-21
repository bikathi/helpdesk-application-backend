package org.poainternet.helpdeskapplication.commentsmodule.abstractions;

import org.poainternet.helpdeskapplication.commentsmodule.entity.Comment;

import java.util.List;

public interface GenericCommentService {
    List<Comment> getCommentList(String issueId, Integer page);
    void saveNewComment(Comment comment);
    Comment getCommentById(String commentId);
}
