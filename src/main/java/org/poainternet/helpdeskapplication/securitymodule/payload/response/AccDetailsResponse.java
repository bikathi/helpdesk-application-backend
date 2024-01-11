package org.poainternet.helpdeskapplication.securitymodule.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccDetailsResponse {
    private String userId;
    private String firstName;
    private String username;
    private String otherName;
    private String profileImage;
    private String email;
    private String dateOfBirth;
    private Set<String> roles;
    private String authToken;
    private String csrfToken;
    private String department;
    private Boolean accountEnabled;
}
