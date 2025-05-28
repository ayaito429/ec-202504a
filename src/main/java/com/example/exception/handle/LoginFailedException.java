package com.example.exception.handle;

/**
 * 認証（ログイン）失敗時にスローされる例外クラス
 * 
 * @author aya_ito
 */
public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String message) {
        super(message);
    }
}
