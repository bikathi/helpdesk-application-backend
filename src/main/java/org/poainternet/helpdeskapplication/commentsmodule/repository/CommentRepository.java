package org.poainternet.helpdeskapplication.commentsmodule.repository;

import org.poainternet.helpdeskapplication.commentsmodule.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
}
