package org.poainternet.helpdeskapplication.issuesmodule.abstractions;

import org.poainternet.helpdeskapplication.issuesmodule.entity.ClientIssue;
import org.poainternet.helpdeskapplication.issuesmodule.payload.response.ClientIssueResponse;
import org.poainternet.helpdeskapplication.securitymodule.SecurityModuleShareable;
import org.poainternet.helpdeskapplication.sharedDTO.SharedUserDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public interface GenericControllerHelper {
    Object convertEntityToPayload(ClientIssue entity, Class<?> target);

    default LocalDate dateStringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH));
    }

    default String localDateToDateString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH));
    }

    default ClientIssueResponse.UserEntity prepareUserEntity(SecurityModuleShareable securityModuleShareable, String userId) {
        SharedUserDTO sharedUserDTO = securityModuleShareable.getMinifiedUserAccountById(userId);

        return new ClientIssueResponse.UserEntity(sharedUserDTO.getUserId(), sharedUserDTO.getUsername(), sharedUserDTO.getFirstName(), sharedUserDTO.getOtherName(), sharedUserDTO.getProfileImage());
    }
}
