package com.wscbs.group12.urlshortner.model;

import lombok.*;

import java.util.List;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscoveryRequest {
    private String name;

    private String port;

    private int duration;

    private String baseUri;

    private List<String> url;
}
