package org.poainternet.helpdeskapplication.sharedDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharedUserDTO {
    private String userId;
    private String firstName;
    private String otherName;
    private String username;
    private String profileImage;
}
