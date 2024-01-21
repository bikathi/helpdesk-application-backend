package org.poainternet.helpdeskapplication.commentsmodule;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.poainternet.helpdeskapplication.commentsmodule.abstractions.GenericCommentsController;
import org.poainternet.helpdeskapplication.commentsmodule.abstractions.GenericControllerHelper;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/comments")
public class CommentsController implements GenericCommentsController, GenericControllerHelper {
    private final String CLASS_NAME = this.getClass().getName();

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @Autowired
    private SecurityModuleShareable securityModuleShareable;

    @Autowired
    private CommentService commentService;

    @Override
    @PostMapping(value = "/new-comment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER') and #request.commenterId == authentication.principal.userid")
    public ResponseEntity<?> addNewComment(@RequestBody AddCommentRequest request) {
        Comment newComment = new Comment(
            this.generateCommentId(),
            request.getIssueId(),
            this.stringToCommentType(request.getCommentType()),
            request.getCommenterId(),
            this.dateStringToLDT(request.getCommentTime()),
            null,
            Objects.isNull(request.getCommentTitle()) ? null : request.getCommentTitle(),
            request.getComment()
        );
        commentService.saveNewComment(newComment);

        log.info("{}: successfully saved new comment for issueId: {}", CLASS_NAME, request.getIssueId());
        return ResponseEntity.ok().body(
            new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully saved new comment",
                HttpStatus.OK.value(),
                null
            )
        );
    }

    @Override
    @PatchMapping(value = "/update-comment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER') and #request.commenterId == authentication.principal.userid")
    public ResponseEntity<?> updateComment(@RequestBody UpdateCommentRequest request) {
        Comment existingComment = commentService.getCommentById(request.getCommentId());
        existingComment.setComment(request.getUpdatedComment());
        existingComment.setLastEdited(this.dateStringToLDT(request.getLastEdited()));

        commentService.saveNewComment(existingComment);
        return ResponseEntity.ok().body(
            new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully updated existing comment",
                HttpStatus.OK.value(),
                null
            )
        );
    }

    @Override
    @GetMapping(value = "/get-list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
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
            .lastEdited(Objects.isNull(comment.getLastEdited()) ? null : this.ldtToDateTimeString(comment.getLastEdited()))
            .commentTime(this.ldtToDateTimeString(comment.getCommentTime()))
            .commentType(this.commentTypeEnumToString(comment.getCommentType()))
            .commentTitle(Objects.isNull(comment.getCommentTitle()) ? null : comment.getCommentTitle())
        .build();
    }
}
