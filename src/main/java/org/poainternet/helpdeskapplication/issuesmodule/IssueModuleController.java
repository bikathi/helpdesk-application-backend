package org.poainternet.helpdeskapplication.issuesmodule;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.poainternet.helpdeskapplication.issuesmodule.abstractions.GenericClientIssueController;
import org.poainternet.helpdeskapplication.issuesmodule.abstractions.GenericControllerHelper;
import org.poainternet.helpdeskapplication.issuesmodule.definitions.ClientIssueState;
import org.poainternet.helpdeskapplication.issuesmodule.entity.ClientIssue;
import org.poainternet.helpdeskapplication.issuesmodule.payload.request.CreateClientIssueRequest;
import org.poainternet.helpdeskapplication.issuesmodule.payload.request.ModIssueRequest;
import org.poainternet.helpdeskapplication.issuesmodule.payload.response.GenericResponse;
import org.poainternet.helpdeskapplication.issuesmodule.service.ClientIssueService;
import org.poainternet.helpdeskapplication.issuesmodule.util.ModuleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/issues")
public class IssueModuleController implements GenericClientIssueController, GenericControllerHelper {
    private final String CLASS_NAME = this.getClass().getName();

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @Autowired
    private ClientIssueService clientIssueService;

    @Autowired
    private ModelMapper mapper;

    @Override
    @PostMapping(value = "/open", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> openNewIssue(@RequestBody CreateClientIssueRequest request) {
        ClientIssue newIssue = ClientIssue.builder()
            .issueId(ModuleUtil.generateIssueId())
            .issueTitle(request.getIssueTitle())
            .openedByUserId(request.getOpenedByUserId())
            .clientName(request.getClientName())
            .clientLocation(
                new ClientIssue.Location(
                    request.getClientLocation().getCounty(), request.getClientLocation().getArea()
                )
            )
            .dateReported(this.dateStringToLocalDate(request.getDateReported()))
            .clientEmail(request.getClientEmail())
            .clientPhone(request.getClientPhone())
            .issueStatus(ClientIssueState.valueOf(request.getIssueStatus()))
            .issueClosed(false) // by default an issue is open i.e -> closed is false
            .issueDescription(request.getIssueDescription())
            .handlerUserIds(new HashSet<>(request.getHandlerUserIds()))
        .build();
        clientIssueService.createNewIssue(newIssue);
        log.info("{}: Successfully created new issue", CLASS_NAME);
        return ResponseEntity.ok(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully retrieved paged accounts list",
            HttpStatus.OK.value(),
            null
        ));
    }

    @Override
    @PatchMapping(value = "/close", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_MODERATOR') && #request.closedByUserId == authentication.principal.userid")
    public ResponseEntity<?> markIssueAsClosed(@RequestBody ModIssueRequest request) {
        clientIssueService.changeIssueClosedStatusTrue(request.getIssueId(), request.getClosedByUserId());
        log.info("{}: Successfully marked issue with id {} as closed", CLASS_NAME, request.getIssueId());

        return ResponseEntity.ok(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully closed issue",
            HttpStatus.OK.value(),
            null
        ));
    }

    @Override
    @PatchMapping(value = "/re-open", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_MODERATOR') && #request.closedByUserId == authentication.principal.userid")
    public ResponseEntity<?> reOpenIssue(@RequestBody ModIssueRequest request) {
        clientIssueService.changeIssueClosedStatusFalse(request.getIssueId(), request.getOpenedByUserId());
        log.info("{}: Successfully re-opened issue with id {}", CLASS_NAME, request.getIssueId());
        return ResponseEntity.ok(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully re-opened issue",
            HttpStatus.OK.value(),
            null
        ));
    }

    @Override
    public ResponseEntity<?> searchIssues() {
        return null;
    }

    @Override
    public ResponseEntity<?> getListOfIssues() {
        return null;
    }

    @Override
    public ResponseEntity<?> getIssueById() {
        return null;
    }

    @Override
    public Object convertEntityToPayload(ClientIssue entity, Class<?> target) {
        return mapper.map(entity, target);
    }
}
