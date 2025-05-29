package com.example.exception;

/**
 * 不正なログイン時にスローされる例外クラス
 * 
 * @author aya_ito
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
