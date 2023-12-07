package com.aidatynybekkyzy.clothshop.exception.exceptionhandler;

import com.aidatynybekkyzy.clothshop.exception.*;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@ControllerAdvice
@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public static final String TRACE = "trace";
    public static final String ACCESS_DENIED = "Access denied!";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String ERROR_MESSAGE_TEMPLATE = "message: %s %n requested uri: %s";
    public static final String LIST_JOIN_DELIMITER = ",";
    public static final String FIELD_ERROR_SEPARATOR = ": ";
    private static final String ERRORS_FOR_PATH = "errors {} for path {}";
    private static final String PATH = "path";
    private static final String ERRORS = "error";
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String TIMESTAMP = "timestamp";
    private static final String TYPE = "type";
    @Value("${reflectoring.trace:false}")
    private boolean printStackTrace;


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> accessDenied(AccessDeniedException exception, WebRequest request) {
        log.error("Authorization has been denied for this request");
        return buildErrorResponse(exception, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: " + ex.getMessage());
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<Object> handlePasswordIncorrectException(PasswordIncorrectException e) {
        String errorMessage = "Incorrect password: " + e.getMessage();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e, WebRequest request) {
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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List<String> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + FIELD_ERROR_SEPARATOR + error.getDefaultMessage())
                .toList();
        return getExceptionResponseEntity(exception, HttpStatus.BAD_REQUEST, request, validationErrors);
    }

    @ExceptionHandler({EntityAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleEntityAlreadyExistsException(EntityAlreadyExistsException e, WebRequest request) {
        log.error("Failed to create/update user " + e);
        return buildErrorResponse(e, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {
        ResponseStatus responseStatus =
                exception.getClass().getAnnotation(ResponseStatus.class);
        final HttpStatus status =
                responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
        final String localizedMessage = exception.getLocalizedMessage();
        final String path = request.getDescription(false);
        String message = (StringUtils.isNotEmpty(localizedMessage) ? localizedMessage : status.getReasonPhrase());
        logger.error(String.format(ERROR_MESSAGE_TEMPLATE, message, path), exception);
        return getExceptionResponseEntity(exception, status, request, Collections.singletonList(message));
    }

    private ResponseEntity<Object> getExceptionResponseEntity(final Exception exception,
                                                              final HttpStatus status,
                                                              final WebRequest request,
                                                              final List<String> errors) {
        final Map<String, Object> body = new LinkedHashMap<>();
        final String path = request.getDescription(false);
        body.put(TIMESTAMP, Instant.now());
        body.put(STATUS, status.value());
        body.put(ERRORS, errors);
        body.put(TYPE, exception.getClass().getSimpleName());
        body.put(PATH, path);
        body.put(MESSAGE, getMessageForStatus(status));
        final String errorsMessage;
        if (!CollectionUtils.isEmpty(errors))
            errorsMessage = errors.stream().filter(StringUtils::isNotEmpty).collect(Collectors.joining(LIST_JOIN_DELIMITER));
        else errorsMessage = status.getReasonPhrase();
        LOG.error(ERRORS_FOR_PATH, errorsMessage, path);
        return new ResponseEntity<>(body, status);
    }

    private String getMessageForStatus(HttpStatus status) {
        switch (status) {
            case UNAUTHORIZED:
                return ACCESS_DENIED;
            case BAD_REQUEST:
                return INVALID_REQUEST;
            default:
                return status.getReasonPhrase();
        }
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

}