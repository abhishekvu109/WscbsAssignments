package com.wscbs.group12.urlshortner.utility.discoveryservice.model.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomError {
    private String field;
    private String description;
    private String code;
}
