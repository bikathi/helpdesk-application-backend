package org.poainternet.helpdeskapplication.securitymodule.abstractions;

import org.poainternet.helpdeskapplication.securitymodule.entity.UserAccount;

public interface EntityModelMapper {
    Object convertEntityToPayload(UserAccount entity, Class<?> target);
}
