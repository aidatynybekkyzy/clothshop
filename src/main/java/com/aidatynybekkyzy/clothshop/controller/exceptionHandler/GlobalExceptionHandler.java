package com.aidatynybekkyzy.clothshop.controller.exceptionHandler;

import com.aidatynybekkyzy.clothshop.exception.CategoryAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException ex) {
        ApiResponse errorResponse = new ApiResponse(HttpStatus.CONFLICT.value()," error ",  ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}