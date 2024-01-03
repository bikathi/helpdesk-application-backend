package org.poainternet.helpdeskapplication.securitymodule.abstractions;

import org.poainternet.helpdeskapplication.securitymodule.definitions.UserRole;
import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.securitymodule.exception.InternalServerError;

import java.util.HashSet;
import java.util.Set;

public interface EntityModelMapperUtil {
    default HashSet<String> roleEnumColToStringCol(Set<UserRole> roles) {
        HashSet<String> userRoles = new HashSet<>();
        roles.forEach(role -> {
            switch(role.name()) {
                case "ROLE_USER" -> userRoles.add("role_user");
                case "ROLE_MODERATOR" -> userRoles.add("role_moderator");
                case "ROLE_MANAGER" -> userRoles.add("role_manager");
            }
        });

        return userRoles;
    }

    default Set<UserRole> stringColToRoleEnumCol(String[] userRoles) throws InternalServerError {
        Set<UserRole> roles = new HashSet<>();
        for(String role : userRoles) {
            switch(role) {
                case "role_user" -> roles.add(UserRole.ROLE_USER);
                case "role_moderator" -> roles.add(UserRole.ROLE_MODERATOR);
                case "role_manager" -> roles.add(UserRole.ROLE_MANAGER);
                default -> throw new InternalServerError(String.format("Undefined role %s", role));
            }
        }
        return roles;
    }
}
