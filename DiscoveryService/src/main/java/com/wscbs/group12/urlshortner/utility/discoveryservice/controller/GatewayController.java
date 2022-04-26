package com.wscbs.group12.urlshortner.utility.discoveryservice.controller;

import com.wscbs.group12.urlshortner.utility.discoveryservice.constants.ApplicationConstants;
import com.wscbs.group12.urlshortner.utility.discoveryservice.entities.Discovery;
import com.wscbs.group12.urlshortner.utility.discoveryservice.entities.ServiceUrl;
import com.wscbs.group12.urlshortner.utility.discoveryservice.model.rest.ResponseEntityBuilder;
import com.wscbs.group12.urlshortner.utility.discoveryservice.model.rest.RestApiResponse;
import com.wscbs.group12.urlshortner.utility.discoveryservice.repository.DiscoveryRepository;
import com.wscbs.group12.urlshortner.utility.discoveryservice.services.DiscoveryService;
import com.wscbs.group12.urlshortner.utility.discoveryservice.services.LoadBalancerService;
import com.wscbs.group12.urlshortner.utility.discoveryservice.services.impl.ConsistentHashingLoadBalancer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GatewayController {
    private final RestTemplate restTemplate;
    private final DiscoveryService discoveryService;
    private final DiscoveryRepository discoveryRepository;
    private final LoadBalancerService loadBalancerService;

    @PostMapping(value = "/**", produces = ApplicationConstants.APPLICATION_JSON, consumes = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse> post(@Valid @RequestBody Object discoveryRequest, HttpServletRequest request, HttpServletResponse response, DispatcherServlet dispatcherServlet) throws IOException {
        HttpHeaders httpHeaders = getHttpHeadersFromRequest(request);
        String uri = request.getRequestURI();
        ServiceUrl serviceUrl = discoveryService.findUrl(uri, 1);
        if (serviceUrl == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.BAD_REQUEST).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Invalid URI");
        Discovery discovery = discoveryRepository.findById(serviceUrl.getDiscoveryId()).get();
        ConsistentHashingLoadBalancer loadBalancer = loadBalancerService.getLoadBalancer(discovery.getServiceName(), discoveryRepository);
        discovery = loadBalancer.serveRequest(request);
        String requestUrl = discovery.getLocation() + uri;
        HttpEntity<Object> entity = new HttpEntity<>(discoveryRequest, httpHeaders);
        ResponseEntity<RestApiResponse> res = null;
        try {
            res = restTemplate.exchange(requestUrl, HttpMethod.POST, entity, RestApiResponse.class, request.getParameterMap());
//            if (res.getStatusCode() == HttpStatus.OK) {
//                return ResponseEntityBuilder.getBuilder(HttpStatus.CREATED).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, res.getBody());
//            }
//            return ResponseEntityBuilder.getBuilder(res.getStatusCode()).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, res.getBody());
            return res;
        } catch (final HttpClientErrorException e) {
            return ResponseEntityBuilder.getBuilder(e.getStatusCode()).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, e.getResponseBodyAsString());
        } catch (Exception ex) {
            return ResponseEntityBuilder.getBuilder(HttpStatus.INTERNAL_SERVER_ERROR).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unable to process your request");
        }

    }

    @GetMapping(value = "/**", produces = ApplicationConstants.APPLICATION_JSON, consumes = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse> get(HttpServletRequest request, HttpServletResponse response, DispatcherServlet dispatcherServlet) throws IOException {
        HttpHeaders httpHeaders = getHttpHeadersFromRequest(request);
        String uri = request.getRequestURI();
        ServiceUrl serviceUrl = discoveryService.findUrl(uri, 1);
        if (serviceUrl == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.BAD_REQUEST).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Invalid URI");
        Discovery discovery = discoveryRepository.findById(serviceUrl.getDiscoveryId()).get();
        ConsistentHashingLoadBalancer loadBalancer = loadBalancerService.getLoadBalancer(discovery.getServiceName(), discoveryRepository);
        discovery = loadBalancer.serveRequest(request);
        String requestUrl = discovery.getLocation() + uri;
        HttpEntity<Map<String, String[]>> entity = new HttpEntity<>(request.getParameterMap(), httpHeaders);
        ResponseEntity<RestApiResponse> res = null;
        try {
            res = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, RestApiResponse.class);
//            if (res.getStatusCode() == HttpStatus.OK) {
//                return res;
////                return ResponseEntityBuilder.getBuilder(HttpStatus.CREATED).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, res.getBody());
//            }
            return res;
//            return ResponseEntityBuilder.getBuilder(res.getStatusCode()).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, res.getBody());
        } catch (final HttpClientErrorException e) {
            return ResponseEntityBuilder.getBuilder(e.getStatusCode()).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, e.getResponseBodyAsString());
        } catch (Exception ex) {
            return ResponseEntityBuilder.getBuilder(HttpStatus.INTERNAL_SERVER_ERROR).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unable to process your request");
        }

    }

    @PutMapping(value = "/**", produces = ApplicationConstants.APPLICATION_JSON, consumes = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse> put(@Valid @RequestBody Object discoveryRequest, HttpServletRequest request, HttpServletResponse response, DispatcherServlet dispatcherServlet) throws IOException {
        HttpHeaders httpHeaders = getHttpHeadersFromRequest(request);
        String uri = request.getRequestURI();
        ServiceUrl serviceUrl = discoveryService.findUrl(uri, 1);
        if (serviceUrl == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.BAD_REQUEST).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Invalid URI");
        Discovery discovery = discoveryRepository.findById(serviceUrl.getDiscoveryId()).get();
        ConsistentHashingLoadBalancer loadBalancer = loadBalancerService.getLoadBalancer(discovery.getServiceName(), discoveryRepository);
        discovery = loadBalancer.serveRequest(request);
        String requestUrl = discovery.getLocation() + uri;
        HttpEntity<Object> entity = new HttpEntity<>(discoveryRequest, httpHeaders);
        ResponseEntity<RestApiResponse> res = null;
        try {
            res = restTemplate.exchange(requestUrl, HttpMethod.PUT, entity, RestApiResponse.class, request.getParameterMap());
//            if (res.getStatusCode() == HttpStatus.OK) {
//                return ResponseEntityBuilder.getBuilder(HttpStatus.CREATED).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, res.getBody());
//            }
//            return ResponseEntityBuilder.getBuilder(res.getStatusCode()).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, res.getBody());
            return res;
        } catch (final HttpClientErrorException e) {
            return ResponseEntityBuilder.getBuilder(e.getStatusCode()).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, e.getResponseBodyAsString());
        } catch (Exception ex) {
            return ResponseEntityBuilder.getBuilder(HttpStatus.INTERNAL_SERVER_ERROR).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unable to process your request");
        }

    }

    @DeleteMapping(value = "/**", produces = ApplicationConstants.APPLICATION_JSON, consumes = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse> delete(@Valid @RequestBody Object discoveryRequest, HttpServletRequest request, HttpServletResponse response, DispatcherServlet dispatcherServlet) throws IOException {
        HttpHeaders httpHeaders = getHttpHeadersFromRequest(request);
        String uri = request.getRequestURI();
        ServiceUrl serviceUrl = discoveryService.findUrl(uri, 1);
        if (serviceUrl == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.BAD_REQUEST).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Invalid URI");
        Discovery discovery = discoveryRepository.findById(serviceUrl.getDiscoveryId()).get();
        ConsistentHashingLoadBalancer loadBalancer = loadBalancerService.getLoadBalancer(discovery.getServiceName(), discoveryRepository);
        discovery = loadBalancer.serveRequest(request);
        String requestUrl = discovery.getLocation() + uri;
        HttpEntity<Object> entity = new HttpEntity<>(discoveryRequest, httpHeaders);
        ResponseEntity<RestApiResponse> res = null;
        try {
            res = restTemplate.exchange(requestUrl, HttpMethod.DELETE, entity, RestApiResponse.class, request.getParameterMap());
//            if (res.getStatusCode() == HttpStatus.OK) {
//                return ResponseEntityBuilder.getBuilder(HttpStatus.CREATED).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, res.getBody());
//            }
//            return ResponseEntityBuilder.getBuilder(res.getStatusCode()).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, res.getBody());
            return res;
        } catch (final HttpClientErrorException e) {
            return ResponseEntityBuilder.getBuilder(e.getStatusCode()).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, e.getResponseBodyAsString());
        } catch (Exception ex) {
            return ResponseEntityBuilder.getBuilder(HttpStatus.INTERNAL_SERVER_ERROR).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unable to process your request");
        }

    }

    private HttpHeaders getHttpHeadersFromRequest(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list(request.getHeaders(h)),
                        (oldValue, newValue) -> newValue,
                        HttpHeaders::new
                ));
    }

}
