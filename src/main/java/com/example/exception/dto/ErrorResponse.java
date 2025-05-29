package com.example.exception.dto;

/**
 * APIエラー応答を表すDTOクラス
 * 
 * @author aya_ito
 */
public class ErrorResponse {

    private Integer status;
    private String message;

    public ErrorResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
