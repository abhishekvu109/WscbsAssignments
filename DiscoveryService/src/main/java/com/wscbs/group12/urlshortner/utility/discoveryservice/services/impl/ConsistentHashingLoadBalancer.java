package com.wscbs.group12.urlshortner.utility.discoveryservice.services.impl;


import com.wscbs.group12.urlshortner.utility.discoveryservice.entities.Discovery;
import com.wscbs.group12.urlshortner.utility.discoveryservice.model.loadbalancer.InstanceModel;
import com.wscbs.group12.urlshortner.utility.discoveryservice.repository.DiscoveryRepository;
import com.wscbs.group12.urlshortner.utility.discoveryservice.services.DiscoveryService;
import com.wscbs.group12.urlshortner.utility.discoveryservice.util.CommonUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.*;

/*
 * We have implemented our algorithm based on the suggestions given in the article
 * https://ably.com/blog/implementing-efficient-consistent-hashing
 * */

@ToString
@EqualsAndHashCode
@Slf4j
@Component
public class ConsistentHashingLoadBalancer {


    private static Integer RINGSIZE = 360;

    private CommonUtil commonUtil = new CommonUtil();

    private List<Discovery> discoveryList;

    private String serviceName;

    private int numberOfNodes;

    private TreeMap<Long, List<Discovery>> nodeHashRing;

    private Map<HttpServletRequest, InstanceModel> requestToInstance;


    public void init(String serviceName, DiscoveryRepository discoveryRepository) {
        this.serviceName = serviceName;
        this.discoveryList = discoveryRepository.findByServiceName(this.serviceName);
        this.numberOfNodes = this.discoveryList.size();
        this.requestToInstance = new HashMap<>();
        buildHashRing();
    }

    private void buildHashRing() {
        this.nodeHashRing = new TreeMap<>();
        for (Discovery discovery : this.discoveryList) {
            Long location = getNodeLocation(discovery);
            List<Discovery> discoveries = this.nodeHashRing.get(location);
            if (CollectionUtils.isEmpty(discoveries))
                discoveries = new LinkedList<>();
            discoveries.add(discovery);
            this.nodeHashRing.put(location, discoveries);
        }
    }

    private Long getNodeLocation(Discovery discovery) {
        return Long.parseLong(discovery.getRefId()) % RINGSIZE;
    }

    public Discovery serveRequest(HttpServletRequest request) {
        Long requestId = Long.parseLong(commonUtil.generateUniqueKey());
        Long hashRingLocation = requestId % RINGSIZE;
        Discovery selectedServer = findInstanceForRequest(hashRingLocation);
        InstanceModel instanceModel = InstanceModel.builder()
                .id(Long.parseLong(commonUtil.generateUniqueKey()))
                .allocatedInstance(selectedServer)
                .hashRingLocation(hashRingLocation)
                .requestId(Integer.valueOf(request.hashCode()))
                .build();
        this.requestToInstance.put(request, instanceModel);
        return selectedServer;
    }

    private Discovery findInstanceForRequest(long requestRingLocation) {
        List<Discovery> discoveries = (this.nodeHashRing.ceilingKey(requestRingLocation) != null) ?
                this.nodeHashRing.get(this.nodeHashRing.ceilingKey(requestRingLocation)) :
                this.nodeHashRing.get(this.nodeHashRing.floorKey(requestRingLocation));
        return discoveries.get(new Random().nextInt(discoveries.size()));
    }

}
