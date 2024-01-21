package org.poainternet.helpdeskapplication.commentsmodule.definitions;

import lombok.Getter;

@Getter
public enum CommentType {
    HEADED("headed"),
    HEADLESS("headless"),
    ACTION("action");

    private final String label;

    CommentType(String label) { this.label = label; }
    public static CommentType getEnumFromString(String str) {
        for (CommentType state : CommentType.values()) {
            if (state.getLabel().equalsIgnoreCase(str)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid string value for ClientIssueState: " + str);
    }
}
