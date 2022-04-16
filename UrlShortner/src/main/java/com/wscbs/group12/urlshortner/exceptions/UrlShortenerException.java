package com.wscbs.group12.urlshortner.exceptions;

import com.wscbs.group12.urlshortner.utils.ResponseErrorCode;

import java.util.Map;

public class UrlShortenerException extends RuntimeException {
    private static final long serialVersionUID = -8081505081645810721L;

    private ResponseErrorCode errorCode;
    private String code;
    private String message;
    private Map<String, Object> errorMap;

    public UrlShortenerException(ResponseErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public UrlShortenerException(ResponseErrorCode errorCode, String code, String message) {
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
        this.message = message;
    }

    public UrlShortenerException(ResponseErrorCode errorCode, Map<String, Object> errorMap) {
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
        this.errorMap = errorMap;
    }
}
