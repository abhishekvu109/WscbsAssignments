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
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        List<ServiceUrl> serviceUrls = new LinkedList<>();
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

            for (String url : request.getUrl()) {
                serviceUrls.add(ServiceUrl.builder().url(url).discoveryId(discovery.getId()).build());
            }
            serviceUrls = serviceUrlRepository.saveAllAndFlush(serviceUrls);
        } else {
            serviceUrls = serviceUrlRepository.findByDiscoveryId(discovery.getId());
        }
        return DiscoveryResponse.builder()
                .refId(discovery.getUuid())
                .serviceName(discovery.getServiceName())
                .port(discovery.getPort())
                .endpoints(serviceUrls.stream().map(serviceUrl -> serviceUrl.getUrl()).collect(Collectors.toSet()))
                .status(discovery.getStatus())
                .build();
    }

    @Override
    public ServiceUrl findUrl(String url, int rowCount) {
        List<ServiceUrl> serviceUrl = serviceUrlRepository.findByUrl(url, PageRequest.of(0, rowCount));
        if (CollectionUtils.isEmpty(serviceUrl)) {
            String[] input = url.split(ApplicationConstants.SEP);
            List<ServiceUrl> serviceUrls = serviceUrlRepository.findAll();
            for (ServiceUrl targetUrl : serviceUrls) {
                String target = targetUrl.getUrl();
                String[] targetArr = target.split(ApplicationConstants.SEP);
                boolean flag = true;
                if (targetArr.length != input.length)
                    continue;
                for (int i = 0; i < input.length; i++) {
                    if (!isQueryParam(targetArr[i])) {
                        if (!targetArr[i].equals(input[i])) {
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag)
                    return targetUrl;
            }
        }
        return serviceUrl.get(0);
    }

    private boolean isQueryParam(String url) {
        return StringUtils.hasLength(url)
                && (url.charAt(0) == ApplicationConstants.PARAM_START)
                && (url.charAt(url.length() - 1) == ApplicationConstants.PARAM_END);
    }


    @Override
    public void undiscover() {

    }

    @Override
    public List<DiscoveryResponse> getAll() {
        List<Discovery> discoveries = discoveryRepository.findAll();
        List<DiscoveryResponse> discoveryResponses = new LinkedList<>();
        if (CollectionUtils.isNotEmpty(discoveries)) {
            for (Discovery discovery : discoveries) {
                List<ServiceUrl> serviceUrls = serviceUrlRepository.findByDiscoveryId(discovery.getId());
                DiscoveryResponse discoveryResponse = DiscoveryResponse.builder()
                        .refId(discovery.getUuid())
                        .serviceName(discovery.getServiceName())
                        .status(discovery.getStatus())
                        .port(discovery.getPort())
                        .endpoints(serviceUrls.stream().map(serviceUrl -> serviceUrl.getUrl()).collect(Collectors.toSet()))
                        .build();
                discoveryResponses.add(discoveryResponse);
            }
        }
        return discoveryResponses;
    }
}
