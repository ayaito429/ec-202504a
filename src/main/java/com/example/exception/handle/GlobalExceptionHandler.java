package com.example.exception.handle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.controller.apiController.UserApiController;
import com.example.exception.UnauthorizedAccessException;
import com.example.exception.LoginFailedException;
import com.example.exception.dto.ErrorResponse;

/**
 * Controllerで起こりえる例外をまとめたクラス
 * 
 * @author aya_ito
 */
@RestControllerAdvice(assignableTypes = UserApiController.class)
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
