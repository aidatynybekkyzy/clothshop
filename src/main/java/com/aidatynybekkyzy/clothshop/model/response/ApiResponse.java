package com.aidatynybekkyzy.clothshop.model.response;

public class ApiResponse {
    private int code;
    private String type;
    private String message;

    public ApiResponse(int code, String type, String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }
}
