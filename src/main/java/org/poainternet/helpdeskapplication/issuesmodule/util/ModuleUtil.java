package org.poainternet.helpdeskapplication.issuesmodule.util;

import org.poainternet.helpdeskapplication.securitymodule.definitions.SearchCriteriaDefinition;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.UUID;
import java.util.regex.Pattern;

public final class ModuleUtil {
    public static Query generateSearchQuery(SearchCriteriaDefinition request) {
        Query query = new Query();

        // build the query
        if(request.getSearchParams().getSearchById()) {
            query.addCriteria(Criteria.where("userId").regex(Pattern.compile(Pattern.quote(request.getSearchTerm()), Pattern.CASE_INSENSITIVE)));
        } else if(request.getSearchParams().getSearchByUsername()) {
            query.addCriteria(Criteria.where("username").regex(Pattern.compile(Pattern.quote(request.getSearchTerm()), Pattern.CASE_INSENSITIVE)));
        } else if(request.getSearchParams().getSearchByFirstOrOtherName()) {
            query.addCriteria(
                new Criteria().orOperator(
                    Criteria.where("firstName").regex(Pattern.compile(Pattern.quote(request.getSearchTerm()), Pattern.CASE_INSENSITIVE)),
                    Criteria.where("otherName").regex(Pattern.compile(Pattern.quote(request.getSearchTerm()), Pattern.CASE_INSENSITIVE))
                )
            );
        }

        // add some pagination
        final Pageable pageableRequest = PageRequest.of(request.getPage(), 10);

        // return the search query
        return query.with(pageableRequest);
    }

    public static String generateIssueId() {
        return UUID.randomUUID().toString().replace("-", "").trim().substring(0, 12);
    }
}
