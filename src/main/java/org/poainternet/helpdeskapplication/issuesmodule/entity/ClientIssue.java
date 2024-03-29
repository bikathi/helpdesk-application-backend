package org.poainternet.helpdeskapplication.issuesmodule.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poainternet.helpdeskapplication.issuesmodule.definitions.Location;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "client-issues")
public class ClientIssue implements Serializable {
    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getLeastSignificantBits();

    @Id
    private String issueId;

    @NotEmpty
    @Max(value = 50) // title must not be more than 50 characters long
    private String issueTitle;

    @NotEmpty
    private String openedByUserId;

    private String closedByUserId;

    @NotEmpty
    private String clientName;

    @NotEmpty
    private Location clientLocation;

    @NotEmpty
    private LocalDate dateReported;

    @Email
    private String clientEmail;

    @NotEmpty
    private String clientPhone;

    @NotEmpty
    private Boolean issueClosed;

    @NotEmpty
    @Max(value = 700)
    private String issueDescription;

    @NotEmpty
    private Set<String> handlerUserIds;
}
