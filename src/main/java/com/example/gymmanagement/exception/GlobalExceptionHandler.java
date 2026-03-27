package com.example.gymmanagement.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.gymmanagement.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

// @RestControllerAdvice: 모든 @RestController에서 발생하는 예외를 이 클래스에서 중앙 처리한다. 
// PHP의 전역 예외 핸들러(set_exception_handler)와 같은 역할이다.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler: 특정 예외 타입이 발생했을 때 실행할 메서드를 지정한다.
    // MemberService에서 throw new IllegalArgumentException("...") 하면 여기서 잡힌다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, e.getMessage()));
    }

    // @Valid 검증 실패 시 Spring이 자동으로 던지는 예외를 여기서 잡는다.
    // e.getBindingResult().getFieldErrors() 는 검증 실패한 필드 목록이다.
    // .get(0).getDefaultMessage() 로 첫 번째 실패 메시지(@NotBlank의 message 값)를 꺼낸다.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, message));
    }
}
