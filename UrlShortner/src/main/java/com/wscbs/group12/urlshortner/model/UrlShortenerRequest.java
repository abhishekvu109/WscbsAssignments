package com.wscbs.group12.urlshortner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.commons.validator.routines.UrlValidator;

import javax.validation.Constraint;
import javax.validation.constraints.*;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlShortenerRequest {

    @NotEmpty(message = "URL is required")
    private String url;

    @JsonIgnore
    private String userId;

    @NotEmpty
    @Min(45)
    @Max(4320)
    private int duration;
}
