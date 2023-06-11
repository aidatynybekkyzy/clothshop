package com.aidatynybekkyzy.clothshop.exception.exceptionHandler;

import com.aidatynybekkyzy.clothshop.exception.*;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;


@ControllerAdvice
@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String TRACE = "trace";
    @Value("${reflectoring.trace:false}")
    private boolean printStackTrace;

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ApiResponse errorResponse = new ApiResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error. Check 'errors' field for details.", ex.getMessage());
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler({CategoryNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException e, WebRequest request) {
        log.error("CategoryNotFoundException was thrown ");
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({InvalidArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidArgumentException(InvalidArgumentException e, WebRequest request) {
        log.error("InvalidArgumentException was thrown ");
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({UserEmailAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserEmailAlreadyExistsException e, WebRequest request) {
        log.error("Failed to create/update user " + e);
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }
    @ExceptionHandler({CategoryAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException e, WebRequest request) {
        log.error("Failed to create/update user " + e);
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e, WebRequest request) {
        log.error("UserNotFoundException was thrown " + e);
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ProductNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException e, WebRequest request) {
        log.error("ProductNotFoundException was thrown ");
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }
    @ExceptionHandler({ProductAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleProductAlreadyExistsException(ProductAlreadyExistsException e, WebRequest request) {
        log.error("Failed to create/update user " + e);
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }
    @ExceptionHandler({ItemNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException e, WebRequest request) {
        log.error("ItemNotFoundException was thrown ");
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }
    @ExceptionHandler({OrderNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException e, WebRequest request) {
        log.error("OrderNotFoundException was thrown ");
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }
    @ExceptionHandler({VendorNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleVendorNotFoundException(VendorNotFoundException e, WebRequest request) {
        log.error("VendorNotFoundException was thrown ");
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }
    @ExceptionHandler({VendorAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleVendorNotFoundException(VendorAlreadyExistsException e, WebRequest request) {
        log.error("VendorAlreadyExistsException was thrown ");
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }



    /**
     * Этот метод buildErrorResponse создает и возвращает объект ResponseEntity<Object> для обработки ошибок.
     * Он принимает следующие параметры:
     * exception: Исключение, которое произошло.
     * httpStatus: HTTP-статус, который будет возвращен в ответе.
     * request: Запрос, который был сделан.
     * <p>
     * Метод вызывает другой вспомогательный метод buildErrorResponse, передавая ему полученное исключение и
     * его сообщение об ошибке, а также HTTP-статус и запрос. Этот вспомогательный метод создает
     * объект ResponseEntity<Object>, содержащий информацию об ошибке, которую можно вернуть в ответ на запрос.
     */
    private ResponseEntity<Object> buildErrorResponse(Exception exception,
                                                      HttpStatus httpStatus,
                                                      WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception,
                                                      String message,
                                                      HttpStatus httpStatus,
                                                      WebRequest request) {
        ApiResponse errorResponse = new ApiResponse(httpStatus.value(), message);
        if (printStackTrace && isTraceOn(request)) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private boolean isTraceOn(WebRequest request) {
        String[] value = request.getParameterValues(TRACE);
        return Objects.nonNull(value)
                && value.length > 0
                && value[0].contentEquals("true");
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        return buildErrorResponse(ex, status, request);
    }

}