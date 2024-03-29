package org.poainternet.helpdeskapplication.commentsmodule.repository;

import org.poainternet.helpdeskapplication.commentsmodule.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    Page<Comment> findAllByIssueId(String issueId, Pageable pageInfo);
}
