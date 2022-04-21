package com.wscbs.group12.urlshortner.utility.discoveryservice.services.impl;

import com.wscbs.group12.urlshortner.utility.discoveryservice.constants.ApplicationConstants;
import com.wscbs.group12.urlshortner.utility.discoveryservice.entities.Discovery;
import com.wscbs.group12.urlshortner.utility.discoveryservice.entities.ServiceUrl;
import com.wscbs.group12.urlshortner.utility.discoveryservice.model.discovery.DiscoveryRequest;
import com.wscbs.group12.urlshortner.utility.discoveryservice.model.discovery.DiscoveryResponse;
import com.wscbs.group12.urlshortner.utility.discoveryservice.repository.DiscoveryRepository;
import com.wscbs.group12.urlshortner.utility.discoveryservice.repository.ServiceUrlRepository;
import com.wscbs.group12.urlshortner.utility.discoveryservice.services.DiscoveryService;
import com.wscbs.group12.urlshortner.utility.discoveryservice.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiscoveryServiceImpl implements DiscoveryService {

    private final DiscoveryRepository discoveryRepository;
    private final ServiceUrlRepository serviceUrlRepository;
    @Autowired
    private CommonUtil commonUtil;

    @Override
    @Transactional
    public DiscoveryResponse discover(DiscoveryRequest request) {
        Discovery discovery = discoveryRepository.findByServiceNameAndPort(request.getName(), Integer.parseInt(request.getPort()));
        if (discovery == null) {
            discovery = Discovery.builder()
                    .port(Integer.parseInt(request.getPort()))
                    .serviceName(request.getName())
                    .uuid(UUID.randomUUID().toString())
                    .location(request.getBaseUri())
                    .refId(commonUtil.generateUniqueKey())
                    .status(ApplicationConstants.AppStatus.ACTIVE.toString())
                    .build();
            discovery = discoveryRepository.saveAndFlush(discovery);
            Set<ServiceUrl> serviceUrls = new HashSet<>();
            for (String url : request.getUrl()) {
                serviceUrls.add(ServiceUrl.builder().url(url).discoveryId(discovery.getId()).build());
            }
            serviceUrlRepository.saveAllAndFlush(serviceUrls);
        }
        return DiscoveryResponse.builder()
                .refId(discovery.getUuid())
                .serviceName(discovery.getServiceName())
                .status(discovery.getStatus())
                .build();
    }

    @Override
    public void undiscover() {

    }
}
