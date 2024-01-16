package org.poainternet.helpdeskapplication.securitymodule.util;

import org.poainternet.helpdeskapplication.securitymodule.definitions.AccountsSearchCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.UUID;
import java.util.regex.Pattern;

public final class ModuleUtil {
    public static Query generateSearchQuery(AccountsSearchCriteria request) {
        Query query = new Query();
        java.util.regex.Pattern pattern = Pattern.compile(Pattern.quote(request.getSearchTerm()), Pattern.CASE_INSENSITIVE);

        // build the query
        if(request.getSearchParams().getSearchById()) {
            query.addCriteria(Criteria.where("userId").regex(pattern));
        } else if(request.getSearchParams().getSearchByUsername()) {
            query.addCriteria(Criteria.where("username").regex(pattern));
        } else if(request.getSearchParams().getSearchByFirstOrOtherName()) {
            query.addCriteria(
                new Criteria().orOperator(
                    Criteria.where("firstName").regex(pattern),
                    Criteria.where("otherName").regex(pattern)
                )
            );
        }

        // add some pagination
        final Pageable pageableRequest = PageRequest.of(request.getPage(), 10);

        // return the search query
        return query.with(pageableRequest);
    }

    public static String generateAccountId() {
        return UUID.randomUUID().toString().replace("-", "").trim().substring(0, 12);
    }
}
