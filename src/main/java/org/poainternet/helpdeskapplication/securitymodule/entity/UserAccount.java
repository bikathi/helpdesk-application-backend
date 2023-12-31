package org.poainternet.helpdeskapplication.securitymodule.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poainternet.helpdeskapplication.securitymodule.definitions.UserRole;
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
@Document(collection = "user-accounts")
public class UserAccount implements Serializable {
    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getLeastSignificantBits();

    @Id
    private String userId;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String username;

    @NotEmpty
    private String otherName;

    private String password;

    private Boolean accountEnabled;

    private String profileImage;

    @Email
    private String email;

    @Past
    @NotEmpty
    private LocalDate dateOfBirth;

    private Set<UserRole> roles;

    @NotEmpty
    private String department;
}
