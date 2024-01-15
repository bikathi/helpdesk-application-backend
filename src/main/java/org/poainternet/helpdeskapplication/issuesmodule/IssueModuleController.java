package org.poainternet.helpdeskapplication.issuesmodule;

import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.issuesmodule.service.ClientIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/issues")
public class IssueModuleController {
    private final String CLASS_NAME = this.getClass().getName();

    @Autowired
    private ClientIssueService clientIssueService;
}
