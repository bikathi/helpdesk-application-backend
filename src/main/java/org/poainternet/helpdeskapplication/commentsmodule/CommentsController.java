package org.poainternet.helpdeskapplication.commentsmodule;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.poainternet.helpdeskapplication.commentsmodule.abstractions.GenericCommentsController;
import org.poainternet.helpdeskapplication.commentsmodule.payload.response.AddCommentRequest;
import org.poainternet.helpdeskapplication.commentsmodule.payload.response.UpdateCommentRequest;
import org.poainternet.helpdeskapplication.commentsmodule.service.CommentService;
import org.poainternet.helpdeskapplication.securitymodule.SecurityModuleShareable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/comments")
public class DiscussionsController implements GenericCommentsController {
    private final String CLASS_NAME = this.getClass().getName();

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private SecurityModuleShareable securityModuleShareable;

    @Autowired
    private CommentService commentService;

    @Override
    public ResponseEntity<?> addNewComment(@RequestBody AddCommentRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateComment(@RequestBody UpdateCommentRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<?> getCommentList(@RequestParam String issueId, @RequestParam Integer page) {
        return null;
    }
}
