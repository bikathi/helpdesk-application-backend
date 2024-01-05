package org.poainternet.helpdeskapplication.sharedexceptions;

public class InternalServerError extends RuntimeException {
    public InternalServerError() {
    }

    public InternalServerError(String message) {
        super(message);
    }
}
