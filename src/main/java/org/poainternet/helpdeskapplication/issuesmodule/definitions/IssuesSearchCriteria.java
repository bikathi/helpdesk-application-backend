package org.poainternet.helpdeskapplication.issuesmodule.definitions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssuesSearchCriteria {
    private String searchTerm;
    private Integer page;
    private SearchParams SearchParams;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchParams {
        private Boolean searchById;
        private Boolean searchByClientEmail;
        private Boolean searchByClientPhone;
        private Boolean searchByHandlerId;
    }
}
