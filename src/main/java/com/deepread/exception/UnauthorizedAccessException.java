package com.deepread.exception;


// 인증되지 않은 접근을 나타내는 커스텀 예외
public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
