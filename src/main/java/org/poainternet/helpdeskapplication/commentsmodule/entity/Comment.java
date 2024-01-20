package org.poainternet.helpdeskapplication.commentsmodule.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "discussion-comments")
public class Comment implements Serializable {
    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getLeastSignificantBits();

    @Id
    private String commentId;

    @NotEmpty
    private String issueId;

    @NotEmpty
    private String commenterId;

    private LocalDateTime commentTime;
    private LocalDateTime lastEdited;
    private String commentTitle;

    @NotEmpty
    private String comment; // a string composed of HTML characters
}
