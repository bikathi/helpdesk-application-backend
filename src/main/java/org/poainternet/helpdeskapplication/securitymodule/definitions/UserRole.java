package org.poainternet.helpdeskapplication.securitymodule.definitions;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_USER("role_user"),
    ROLE_MODERATOR("role_moderator"),
    ROLE_ADMIN("role_admin");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public static UserRole getEnumFromString(String str) {
        for (UserRole state : UserRole.values()) {
            if (state.getLabel().equalsIgnoreCase(str)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid string value for ClientIssueState: " + str);
    }

}
