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
}
