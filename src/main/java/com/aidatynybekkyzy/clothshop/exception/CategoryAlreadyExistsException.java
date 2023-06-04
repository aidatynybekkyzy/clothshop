package com.aidatynybekkyzy.clothshop.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String message) {
        super("Category with this name already exists"+message);
    }
}
