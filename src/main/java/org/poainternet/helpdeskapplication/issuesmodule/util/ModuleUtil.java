package org.poainternet.helpdeskapplication.issuesmodule.util;

import org.poainternet.helpdeskapplication.issuesmodule.definitions.IssuesSearchCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.UUID;
import java.util.regex.Pattern;

public final class ModuleUtil {
    public static Query generateSearchQuery(IssuesSearchCriteria request) {
        Query query = new Query();
        java.util.regex.Pattern pattern = Pattern.compile(Pattern.quote(request.getSearchTerm()), Pattern.CASE_INSENSITIVE);

        // build the query
        if(request.getSearchParams().getSearchById()) {
            query.addCriteria(Criteria.where("issueId").regex(pattern));
        } else if(request.getSearchParams().getSearchByClientEmail()) {
            query.addCriteria(Criteria.where("clientEmail").regex(pattern));
        } else if(request.getSearchParams().getSearchByClientPhone()) {
            query.addCriteria(Criteria.where("clientPhone").regex(pattern));
        } else if(request.getSearchParams().getSearchByHandlerId()) {
            query.addCriteria(Criteria.where("handlerUserIds").in(Collections.singletonList(request.getSearchTerm())));
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
