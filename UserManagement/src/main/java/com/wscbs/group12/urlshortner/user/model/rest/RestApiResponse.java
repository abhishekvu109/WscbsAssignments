package com.wscbs.group12.urlshortner.user.model.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wscbs.group12.urlshortner.user.constants.ApplicationConstants;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestApiResponse {

    private Meta meta;
    private Object data;
    private List<CustomError> errors;

    public static RestApiResponse buildSuccessResponse(String code, String message, Object result) {
        RestApiResponse response = new RestApiResponse();
        Meta meta = new Meta();
        meta.setCode(code);
        meta.setDescription(message);
        meta.setStatus(ApplicationConstants.STATUS_SUCCESS);
        response.setMeta(meta);
        response.setData(result);
        return response;
    }

    public static RestApiResponse buildFailureResponse(String code, String message) {
        RestApiResponse response = new RestApiResponse();
        Meta meta = new Meta();
        meta.setCode(code);
        meta.setDescription(message);
        meta.setStatus(ApplicationConstants.STATUS_FAILURE);
        response.setMeta(meta);
        return response;
    }
}
