package org.poainternet.helpdeskapplication.securitymodule;

import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;
import org.poainternet.helpdeskapplication.securitymodule.service.UserAccountService;
import org.poainternet.helpdeskapplication.sharedDTO.SharedUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityModuleShareable {
    @Autowired
    private UserAccountService userAccountService;

    public SharedUserDTO getMinifiedUserAccountById(String userId) {
        UserAccount userAccount = userAccountService.getAccountById(userId);
        return SharedUserDTO.builder()
            .userId(userAccount.getUserId())
            .firstName(userAccount.getFirstName())
            .otherName(userAccount.getOtherName())
            .username(userAccount.getUsername())
            .profileImage(userAccount.getProfileImage())
        .build();
    }
}
