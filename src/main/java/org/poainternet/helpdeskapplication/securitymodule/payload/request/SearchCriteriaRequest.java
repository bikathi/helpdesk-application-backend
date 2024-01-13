package org.poainternet.helpdeskapplication.securitymodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteriaRequest {
    private String searchDomain;
    private SearchControl searchControl;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchControl {
        private Boolean searchById;
        private Boolean searchByUsername;
        private Boolean searchByFirstName;
        private Boolean searchByLastName;
    }
}
