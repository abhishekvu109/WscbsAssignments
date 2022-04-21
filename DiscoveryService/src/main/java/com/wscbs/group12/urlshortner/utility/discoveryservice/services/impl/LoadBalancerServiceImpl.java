package com.wscbs.group12.urlshortner.utility.discoveryservice.services.impl;

import com.wscbs.group12.urlshortner.utility.discoveryservice.model.loadbalancer.InstanceModel;
import com.wscbs.group12.urlshortner.utility.discoveryservice.repository.DiscoveryRepository;
import com.wscbs.group12.urlshortner.utility.discoveryservice.services.LoadBalancerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Scope("singleton")
public class LoadBalancerServiceImpl implements LoadBalancerService {

    private Map<String, ConsistentHashingLoadBalancer> instanceModelMap = new HashMap<>();


    @Override
    public ConsistentHashingLoadBalancer getLoadBalancer(String serviceName, DiscoveryRepository discoveryRepository) {
        ConsistentHashingLoadBalancer loadBalancer = instanceModelMap.get(serviceName);
        if (loadBalancer == null) {
            loadBalancer = new ConsistentHashingLoadBalancer();
            loadBalancer.init(serviceName,discoveryRepository);
        }
        return loadBalancer;
    }

}
