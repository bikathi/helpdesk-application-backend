package org.poainternet.helpdeskapplication.securitymodule.exception;

public class InternalServerError extends RuntimeException {
    public InternalServerError() {
    }

    public InternalServerError(String message) {
        super(message);
    }
}
