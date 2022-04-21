package com.wscbs.group12.urlshortner.utility.discoveryservice.services;

import com.wscbs.group12.urlshortner.utility.discoveryservice.entities.Discovery;
import com.wscbs.group12.urlshortner.utility.discoveryservice.model.discovery.DiscoveryRequest;
import com.wscbs.group12.urlshortner.utility.discoveryservice.model.discovery.DiscoveryResponse;

import java.util.List;

public interface DiscoveryService {

    DiscoveryResponse discover(DiscoveryRequest request);

    void undiscover();
}
