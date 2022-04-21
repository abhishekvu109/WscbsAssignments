package com.wscbs.group12.urlshortner.user.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public enum ResponseErrorCode {
    UNKNOWN_ERROR(ErrorCode.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR, HttpStatus.OK);

    private String code;
    private String description;
    private HttpStatus httpStatus;
}
