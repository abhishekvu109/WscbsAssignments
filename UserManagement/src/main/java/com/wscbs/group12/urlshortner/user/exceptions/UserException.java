package com.wscbs.group12.urlshortner.user.exceptions;


import com.wscbs.group12.urlshortner.user.util.ResponseErrorCode;

import java.util.Map;

public class UserException extends RuntimeException {
    private static final long serialVersionUID = -8081505081645810721L;

    private ResponseErrorCode errorCode;
    private String code;
    private String message;
    private Map<String, Object> errorMap;

    public UserException(ResponseErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public UserException(ResponseErrorCode errorCode, String code, String message) {
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
        this.message = message;
    }

    public UserException(ResponseErrorCode errorCode, Map<String, Object> errorMap) {
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
        this.errorMap = errorMap;
    }
}
