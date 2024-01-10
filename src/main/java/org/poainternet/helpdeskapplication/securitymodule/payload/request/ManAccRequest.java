package org.poainternet.helpdeskapplication.securitymodule.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManAccRequest {
    @NotEmpty
    private String userId;

    private String firstName;
    private String otherName;
    private String username;

    @Email
    private String email;
    private String profileURL;

    private String dateOfBirth;
    private String[] roles;
    private String department;
}
