package com.deepread.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Not Found",
                        "message", ex.getMessage(),
                        "path", request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedAccessException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Unauthorized",
                        "message", ex.getMessage(),
                        "path", request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unhandled exception occurred", ex); // 스택 트레이스 포함 로그 기록

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Internal Server Error",
                        "message", ex.getMessage(),
                        "path", request.getDescription(false).replace("uri=", "")
                ));
    }
}

//package com.deepread.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.context.request.WebRequest;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(Map.of(
//                        "timestamp", LocalDateTime.now(),
//                        "error", "Not Found",
//                        "message", ex.getMessage(),
//                        "path", request.getDescription(false).replace("uri=","")
//        ));
//    }
//
//    @ExceptionHandler(UnauthorizedAccessException.class)
//    public ResponseEntity<?> handleUnauthorized(UnauthorizedAccessException ex, WebRequest request) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(Map.of(
//                        "timestamp", LocalDateTime.now(),
//                        "error", "Unauthorized",
//                        "message", ex.getMessage(),
//                        "path", request.getDescription(false).replace("uri=", "")
//                ));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(Map.of(
//                        "timestamp", LocalDateTime.now(),
//                        "error", "Internal Server Error",
//                        "message", ex.getMessage(),
//                        "path", request.getDescription(false).replace("uri=", "")
//                ));
//    }
//    // ✅ Optional: 나중에 추가할 예외들
//    // @ExceptionHandler(InvalidRequestException.class)
//    // public ResponseEntity<String> handleInvalidRequest(...) { ... }
//}
