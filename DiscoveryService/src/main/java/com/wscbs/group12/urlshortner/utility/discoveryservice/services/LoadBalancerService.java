package com.wscbs.group12.urlshortner.utility.discoveryservice.services;

import com.wscbs.group12.urlshortner.utility.discoveryservice.repository.DiscoveryRepository;
import com.wscbs.group12.urlshortner.utility.discoveryservice.services.impl.ConsistentHashingLoadBalancer;

public interface LoadBalancerService {
    ConsistentHashingLoadBalancer getLoadBalancer(String serviceName, DiscoveryRepository discoveryRepository);
}
