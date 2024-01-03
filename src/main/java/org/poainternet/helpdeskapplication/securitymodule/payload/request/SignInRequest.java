package org.poainternet.helpdeskapplication.securitymodule.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
