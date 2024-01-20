package org.poainternet.helpdeskapplication.commentsmodule.service;

import org.poainternet.helpdeskapplication.commentsmodule.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
}
