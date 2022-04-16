package com.wscbs.group12.urlshortner.user.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotNull(message = "User ID can't be null")
    @NotEmpty(message = "User ID can't be empty")
    private String userId;

    @NotNull(message = "Password can't be null")
    @NotEmpty(message = "Password can't be empty")
    private String password;
}
