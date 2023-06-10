package com.aidatynybekkyzy.clothshop.controller.exceptionHandler;

import com.aidatynybekkyzy.clothshop.exception.CategoryAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.exception.CategoryNotFoundException;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException ex) {
        log.info("throwing already exists exception");
        ApiResponse errorResponse = new ApiResponse(HttpStatus.CONFLICT.value(), " error ", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), " error ", ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
}