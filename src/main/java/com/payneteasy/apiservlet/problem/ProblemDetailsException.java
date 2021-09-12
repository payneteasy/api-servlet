package com.payneteasy.apiservlet.problem;

public class ProblemDetailsException extends Exception {

    private final ProblemDetails problemDetails;

    public ProblemDetailsException(ProblemDetails aProblem, String message) {
        super(message);
        problemDetails = aProblem;
    }

    public ProblemDetailsException(ProblemDetails aProblem, String message, Throwable cause) {
        super(message, cause);
        problemDetails = aProblem;
    }

    public ProblemDetails getProblemDetails() {
        return problemDetails;
    }

}
