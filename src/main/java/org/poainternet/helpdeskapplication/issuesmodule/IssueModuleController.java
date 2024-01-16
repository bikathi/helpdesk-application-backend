package org.poainternet.helpdeskapplication.issuesmodule;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.poainternet.helpdeskapplication.issuesmodule.abstractions.GenericClientIssueController;
import org.poainternet.helpdeskapplication.issuesmodule.abstractions.GenericControllerHelper;
import org.poainternet.helpdeskapplication.issuesmodule.definitions.ClientIssueState;
import org.poainternet.helpdeskapplication.issuesmodule.definitions.IssuesSearchCriteria;
import org.poainternet.helpdeskapplication.issuesmodule.definitions.Location;
import org.poainternet.helpdeskapplication.issuesmodule.entity.ClientIssue;
import org.poainternet.helpdeskapplication.issuesmodule.payload.request.CreateClientIssueRequest;
import org.poainternet.helpdeskapplication.issuesmodule.payload.request.ModIssueRequest;
import org.poainternet.helpdeskapplication.issuesmodule.payload.response.ClientIssueResponse;
import org.poainternet.helpdeskapplication.issuesmodule.payload.response.GenericResponse;
import org.poainternet.helpdeskapplication.issuesmodule.service.ClientIssueService;
import org.poainternet.helpdeskapplication.issuesmodule.util.ModuleUtil;
import org.poainternet.helpdeskapplication.securitymodule.AccountsModuleController;
import org.poainternet.helpdeskapplication.securitymodule.SecurityModuleShareable;
import org.poainternet.helpdeskapplication.securitymodule.definitions.AccountsSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private SecurityModuleShareable securityModuleShareable;

    @Override
    @PostMapping(value = "/open", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<?> openNewIssue(@RequestBody CreateClientIssueRequest request) {
        ClientIssue newIssue = ClientIssue.builder()
            .issueId(ModuleUtil.generateIssueId())
            .issueTitle(request.getIssueTitle())
            .openedByUserId(request.getOpenedByUserId())
            .clientName(request.getClientName())
            .clientLocation(new Location(request.getClientLocation().getCounty(), request.getClientLocation().getArea()))
            .dateReported(this.dateStringToLocalDate(request.getDateReported()))
            .clientEmail(Objects.isNull(request.getClientEmail()) ? null : request.getClientEmail())
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
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> searchIssues(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "searchTerm") String searchTerm,
        @RequestParam(name = "byId") Boolean byId,
        @RequestParam(name = "byClientEmail") Boolean byClientEmail,
        @RequestParam(name = "byClientPhone") Boolean byClientPhone,
        @RequestParam(name = "byHandlerId") Boolean byHandlerId
    ) {
        IssuesSearchCriteria searchCriteria = IssuesSearchCriteria.builder()
            .page(page)
            .searchTerm(searchTerm)
            .SearchParams(new IssuesSearchCriteria.SearchParams(byId, byClientEmail, byClientPhone, byHandlerId))
        .build();
        List<ClientIssue> clientIssues = clientIssueService.searchClientIssues(searchCriteria);
        List<ClientIssueResponse> responseList = clientIssues.parallelStream().map(this::generateClientIssueResponse).toList();
        log.info("{}: Successfully retrieved issues list by search for page b{}", CLASS_NAME, page);

        return ResponseEntity.ok(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully retrieved issues list",
            HttpStatus.OK.value(),
            responseList
        ));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/get-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListOfIssues(
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        List<ClientIssue> clientIssues = clientIssueService.getListOfClientIssues(page, size);
        List<ClientIssueResponse> responseList = clientIssues.parallelStream().map(this::generateClientIssueResponse).toList();
        log.info("{}: Successfully retrieved issues list for page {}", CLASS_NAME, page);

        return ResponseEntity.ok(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully retrieved issues list",
            HttpStatus.OK.value(),
            responseList
        ));
    }

    @Override
    @GetMapping(value = "/get-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getIssueById(@RequestParam(name = "id") String clientIssueId) {
        ClientIssue clientIssue = clientIssueService.getClientIssueById(clientIssueId);
        ClientIssueResponse response = generateClientIssueResponse(clientIssue);
        log.info("{}: Successfully retrieved issue with id {}", CLASS_NAME, clientIssueId);

        return ResponseEntity.ok(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully retrieved issue",
            HttpStatus.OK.value(),
            response
        ));
    }

    @Override
    public Object convertEntityToPayload(ClientIssue entity, Class<?> target) {
        return mapper.map(entity, target);
    }

    private ClientIssueResponse generateClientIssueResponse(ClientIssue clientIssue) {
        return ClientIssueResponse.builder()
            .issueId(clientIssue.getIssueId())
            .issueTitle(clientIssue.getIssueTitle())
            .openedByUser(this.prepareUserEntity(securityModuleShareable, clientIssue.getOpenedByUserId()))
            .closedByUser(clientIssue.getIssueClosed() ? this.prepareUserEntity(securityModuleShareable, clientIssue.getClosedByUserId()) : null)
            .clientName(clientIssue.getClientName())
            .clientLocation(clientIssue.getClientLocation())
            .dateReported(this.localDateToDateString(clientIssue.getDateReported()))
            .clientEmail(Objects.isNull(clientIssue.getClientEmail()) ? null : clientIssue.getClientEmail())
            .clientPhone(clientIssue.getClientPhone())
            .issueStatus(clientIssue.getIssueStatus().getLabel())
            .issueClosed(clientIssue.getIssueClosed())
            .issueDescription(clientIssue.getIssueDescription())
            .handlerUsers(clientIssue.getHandlerUserIds().parallelStream().map(handlerId -> this.prepareUserEntity(securityModuleShareable, handlerId)).toList())
        .build();
    }
}
