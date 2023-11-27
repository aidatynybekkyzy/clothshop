package com.aidatynybekkyzy.clothshop.exception;

public class AuthProblemException extends RuntimeException {
    public AuthProblemException(String message) {
        super(message);
    }

    public AuthProblemException(String message, Throwable cause) {
        super(message, cause);
    }
}
