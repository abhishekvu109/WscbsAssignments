package com.wscbs.group12.urlshortner.user.model;

import lombok.*;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String refId;

    private String name;

    private String email;
}
