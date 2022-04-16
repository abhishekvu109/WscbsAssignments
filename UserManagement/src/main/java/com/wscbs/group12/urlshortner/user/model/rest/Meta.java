package com.wscbs.group12.urlshortner.user.model.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Meta {
    private String code;
    private String description;
    private int status;
}
