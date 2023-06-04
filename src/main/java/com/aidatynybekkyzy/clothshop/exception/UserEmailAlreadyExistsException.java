package com.aidatynybekkyzy.clothshop.exception;

public class UserEmailAlreadyExistsException extends RuntimeException {
    public UserEmailAlreadyExistsException(String message){
        super(message);
    }
}
