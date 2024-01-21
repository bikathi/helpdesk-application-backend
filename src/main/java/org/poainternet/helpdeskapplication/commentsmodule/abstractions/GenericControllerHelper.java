package org.poainternet.helpdeskapplication.commentsmodule.abstractions;

import org.poainternet.helpdeskapplication.commentsmodule.definitions.CommentType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public interface GenericControllerHelper {
    default String generateCommentId() { return UUID.randomUUID().toString().substring(0, 12); }

    default String commentTypeEnumToString(CommentType commentType){ return commentType.getLabel(); }

    default CommentType stringToCommentType(String commentType) { return CommentType.getEnumFromString(commentType); }

    default LocalDateTime dateStringToLDT(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm");
        return LocalDateTime.parse(date, formatter);
    }

    default String ldtToDateTimeString(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm");
        return date.format(formatter);
    }
}
