package org.poainternet.helpdeskapplication.securitymodule.definitions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteriaDefinition {
    private String searchTerm;
    private Integer page;
    private SearchParams SearchParams;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchParams {
        private Boolean searchById;
        private Boolean searchByUsername;
        private Boolean searchByFirstOrOtherName;
    }
}
