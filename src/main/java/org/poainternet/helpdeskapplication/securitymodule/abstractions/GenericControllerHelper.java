package org.poainternet.helpdeskapplication.securitymodule.abstractions;

import org.poainternet.helpdeskapplication.securitymodule.definitions.UserRole;
import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.sharedexceptions.InternalServerError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public interface GenericControllerHelper {
    Object convertEntityToPayload(UserAccount entity, Class<?> target);

    default HashSet<String> roleEnumColToStringCol(Set<UserRole> roles) {
        HashSet<String> userRoles = new HashSet<>();
        roles.forEach(role ->  userRoles.add(role.getLabel()));
        return userRoles;
    }

    default Set<UserRole> stringColToRoleEnumCol(String[] userRoles) throws InternalServerError {
        Set<UserRole> roles = new HashSet<>();
        for(String role : userRoles) { roles.add(UserRole.getEnumFromString(role)); }
        return roles;
    }

    default LocalDate dateStringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH));
    }

    default String localDateToDateString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH));
    }
}
