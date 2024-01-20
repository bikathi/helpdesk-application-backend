package org.poainternet.helpdeskapplication.commentsmodule;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.poainternet.helpdeskapplication.commentsmodule.service.CommentService;
import org.poainternet.helpdeskapplication.securitymodule.SecurityModuleShareable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/comments")
public class DiscussionsController {
    private final String CLASS_NAME = this.getClass().getName();

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private SecurityModuleShareable securityModuleShareable;

    @Autowired
    private CommentService commentService;
}
