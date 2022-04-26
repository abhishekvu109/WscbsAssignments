package com.wscbs.group12.urlshortner.utility.discoveryservice.model.discovery;

import lombok.*;

import java.util.Set;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscoveryResponse {

    private String refId;

    private String serviceName;

    private String status;

    private String message;

    private int port;

    private Set<String> endpoints;
}
