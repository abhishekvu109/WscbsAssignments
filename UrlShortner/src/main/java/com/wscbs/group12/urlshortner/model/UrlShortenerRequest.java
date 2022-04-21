package com.wscbs.group12.urlshortner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlShortenerRequest {

    @NotEmpty(message = "URL is required")
    @NotNull(message = "URL is required")
    private String url;

    @JsonIgnore
    private String userId;
    @NotEmpty
    private int duration;
}
