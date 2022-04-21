package com.wscbs.group12.urlshortner.model;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlShortenerResponse {

    private String refId;

    private String url;

    private String shortUrl;

    private LocalDateTime expirationTimeStamp;

}
