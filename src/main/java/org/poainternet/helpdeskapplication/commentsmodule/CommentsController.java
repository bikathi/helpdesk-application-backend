package org.poainternet.helpdeskapplication.commentsmodule;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.poainternet.helpdeskapplication.commentsmodule.abstractions.GenericCommentsController;
import org.poainternet.helpdeskapplication.commentsmodule.entity.Comment;
import org.poainternet.helpdeskapplication.commentsmodule.payload.request.AddCommentRequest;
import org.poainternet.helpdeskapplication.commentsmodule.payload.request.UpdateCommentRequest;
import org.poainternet.helpdeskapplication.commentsmodule.payload.response.CommentDetailsResponse;
import org.poainternet.helpdeskapplication.commentsmodule.service.CommentService;
import org.poainternet.helpdeskapplication.securitymodule.SecurityModuleShareable;
import org.poainternet.helpdeskapplication.securitymodule.payload.response.GenericResponse;
import org.poainternet.helpdeskapplication.sharedDTO.SharedUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/comments")
public class CommentsController implements GenericCommentsController {
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
    public ResponseEntity<?> getCommentList(@RequestParam(name = "issueId") String issueId, @RequestParam(name = "page") Integer page) {
        List<Comment> commentList = commentService.getCommentList(issueId, page);
        List<CommentDetailsResponse> responseList = commentList.parallelStream().map(this::prepareCommentResponse).toList();

        log.info("{}: successfully retrieved page {} comments list for issueId: {}", issueId, CLASS_NAME, page);
        return ResponseEntity.ok().body(
            new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully retrieved paged comments list",
                HttpStatus.OK.value(),
                responseList
            )
        );
    }

    private CommentDetailsResponse prepareCommentResponse(Comment comment) {
        SharedUserDTO userDTO = securityModuleShareable.getMinifiedUserAccountById(comment.getCommenterId());
        return CommentDetailsResponse.builder()
            .commentId(comment.getCommentId())
            .issueId(comment.getIssueId())
            .commenter(new CommentDetailsResponse.Commenter(
                userDTO.getProfileImage(),
                userDTO.getUsername(),
                userDTO.getFirstName(),
                userDTO.getOtherName(),
                userDTO.getUserId()))
                // .lastEdited()
                // .commentTime()
                // .commentType()
                .commentTitle(comment.getCommentTitle())
        .build();
    }
}
