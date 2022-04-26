package com.wscbs.group12.urlshortner.utility.discoveryservice.constants;

public class ApplicationConstants {
    public static final String APPLICATION_JSON = "application/json";
    public static final String REQUEST_SUCCESS_CODE = "000";
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_FAILURE = 1;
    public static final String REQUEST_SUCCESS_DESCRIPTION = "SUCCESS";
    public static final String REQUEST_FAILURE_DESCRIPTION = "FAILED";
    public static final String DOMAIN_NAME = "https://group12.com/";
    public static final int KEY_LENGTH = 6;
    public static final String SEP = "/";
    public static final Character PARAM_START = '{';
    public static final Character PARAM_END = '}';
    public enum AppStatus {
        ACTIVE, INACTIVE
    }
}
