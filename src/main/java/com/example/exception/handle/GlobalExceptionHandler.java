package com.example.exception.handle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.exception.dto.ErrorResponse;

/**
 * Controllerで起こりえる例外をまとめたクラス
 * 
 * @author aya_ito
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 認証エラー LoginFailedExceptionをハンドリング
     * 
     * @param ex LoginFailedException
     * @return 401エラー
     */
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ErrorResponse> handleLoginFailedException(LoginFailedException ex) {
        ErrorResponse body = new ErrorResponse(401, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 不正ログインエラー UnauthorizedAccessException
     * 
     * @param ex UnauthorizedAccessException
     * @return 401エラー
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        ErrorResponse body = new ErrorResponse(401, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 不正リクエストエラー NoResourceFoundException
     * 
     * @param ex NoResourceFoundException
     * @return 404エラー
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        ErrorResponse body = new ErrorResponse(404, "リクエストされたリソースは存在しません。");
        return new ResponseEntity<>(body, HttpStatus.BAD_GATEWAY);
    }

    /**
     * その他の例外をハンドリング
     *
     * @param ex Exception
     * @return 500エラー
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse body = new ErrorResponse(500, "内部エラーが発生しました。");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
