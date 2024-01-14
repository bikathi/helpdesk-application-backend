package org.poainternet.helpdeskapplication.sharedexceptions;

import lombok.extern.slf4j.Slf4j;
import org.poainternet.helpdeskapplication.securitymodule.payload.response.GenericResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SharedControllerAdvice {
    private final String CLASS_NAME = this.getClass().getName();

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {

        log.info("{}: dispatching response for EntityNotFoundException caused by: {}", CLASS_NAME, ex.getMessage());
        return new ResponseEntity<>(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Unique resource not found!",
                HttpStatus.NOT_FOUND.value(),
                null
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleInternalServerError(InternalServerError ex) {
        log.info("{}: dispatching response for InternalServerError caused by: {}", CLASS_NAME, ex.getMessage());
        return ResponseEntity.internalServerError().body(
            new GenericResponse<>(
                apiVersion,
                organizationName,
                "Operation failed on the server!",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                null
            )
        );
    }
}
