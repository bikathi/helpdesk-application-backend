package org.poainternet.helpdeskapplication.issuesmodule.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poainternet.helpdeskapplication.issuesmodule.definitions.Location;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientIssueRequest {
    @Id
    private String issueId;

    @NotEmpty
    @Max(value = 50) // title must not be more than 50 characters long
    private String issueTitle;

    @NotEmpty
    private String openedByUserId;

    @NotEmpty
    private String clientName;

    @NotEmpty
    private Location clientLocation;

    @NotEmpty
    private String dateReported;

    @Email
    private String clientEmail;

    @NotEmpty
    private String clientPhone;

    @NotEmpty
    @Max(value = 700)
    private String issueDescription;

    @NotEmpty
    private List<String> handlerUserIds;
}
