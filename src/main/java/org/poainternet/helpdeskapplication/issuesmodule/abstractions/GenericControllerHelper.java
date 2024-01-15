package org.poainternet.helpdeskapplication.issuesmodule.abstractions;

import org.poainternet.helpdeskapplication.issuesmodule.entity.ClientIssue;

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
}
