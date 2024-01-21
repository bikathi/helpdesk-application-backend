package org.poainternet.helpdeskapplication.commentsmodule.service;

import org.poainternet.helpdeskapplication.commentsmodule.abstractions.GenericCommentService;
import org.poainternet.helpdeskapplication.commentsmodule.entity.Comment;
import org.poainternet.helpdeskapplication.commentsmodule.repository.CommentRepository;
import org.poainternet.helpdeskapplication.sharedexceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService implements GenericCommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentList(String issueId, Integer page) {
        Pageable pageInfo = PageRequest.of(page, 10);
        return commentRepository.findAllByIssueId(issueId, pageInfo).toList();
    }

    @Override
    public void saveNewComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(String commentId) {
        return commentRepository.findById(commentId).orElseThrow(
            () -> new EntityNotFoundException(String.format("Comment with id %s not found", commentId)));
    }
}
