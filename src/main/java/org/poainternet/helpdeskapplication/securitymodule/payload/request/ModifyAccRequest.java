package org.poainternet.helpdeskapplication.securitymodule.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyAccRequest {
    @NotNull
    private String userId;

    private String firstName;
    private String username;
    private String otherName;

    @Email
    private String email;

    private String dateOfBirth;
    private Set<String> roles;
    private String authToken;
    private String department;
}
