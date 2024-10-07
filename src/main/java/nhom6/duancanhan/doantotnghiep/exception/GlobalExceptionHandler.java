package nhom6.duancanhan.doantotnghiep.exception;

import nhom6.duancanhan.doantotnghiep.dto.ErrorResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException ex, WebRequest webRequest) {
        String requestUrl = webRequest.getDescription(false);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .timestamp(new Date())
                .detail("Request URL: " + requestUrl)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException ex, WebRequest webRequest) {
        String requestUrl = webRequest.getDescription(false);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .message("Duplicate key error: " + ex.getMessage())
                .timestamp(new Date())
                .detail("Request URL: " + requestUrl)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({DataAccessException.class, JpaSystemException.class})
    public ResponseEntity<ErrorResponse> handleDatabaseExceptions(RuntimeException e, WebRequest webRequest) {
        String requestUrl = webRequest.getDescription(false);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("Database error: " + e.getMessage())
                .timestamp(new Date())
                .detail("Request URL: " + requestUrl)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

