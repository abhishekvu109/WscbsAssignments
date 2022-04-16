package com.wscbs.group12.urlshortner.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotNull(message = "Name can't be null")
    @NotEmpty(message = "Name can't be empty")
    private String name;

    @NotNull(message = "User ID can't be null")
    @NotEmpty(message = "User ID can't be empty")
    private String userId;

    @NotNull(message = "Password can't be null")
    @NotEmpty(message = "Password can't be empty")
    private String password;

    @NotNull(message = "Email can't be null")
    @NotEmpty(message = "Email can't be empty")
    private String email;
}
