package com.wscbs.group12.urlshortner.utility.discoveryservice.controller;

import com.wscbs.group12.urlshortner.utility.discoveryservice.constants.ApplicationConstants;
import com.wscbs.group12.urlshortner.utility.discoveryservice.constants.UrlConstants;
import com.wscbs.group12.urlshortner.utility.discoveryservice.model.discovery.DiscoveryRequest;
import com.wscbs.group12.urlshortner.utility.discoveryservice.model.discovery.DiscoveryResponse;
import com.wscbs.group12.urlshortner.utility.discoveryservice.model.rest.ResponseEntityBuilder;
import com.wscbs.group12.urlshortner.utility.discoveryservice.model.rest.RestApiResponse;
import com.wscbs.group12.urlshortner.utility.discoveryservice.services.DiscoveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiscoveryController {

    private final DiscoveryService discoveryService;


    @PostMapping(value = UrlConstants.DISCOVERY, produces = ApplicationConstants.APPLICATION_JSON, consumes = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse> discovery(@Valid @RequestBody DiscoveryRequest discoveryRequest) {
        log.info("Received a request to discover a new service- {}", discoveryRequest);
        DiscoveryResponse discoveryResponse = null;
        try {
            discoveryResponse = discoveryService.discover(discoveryRequest);
            return ResponseEntityBuilder.getBuilder(HttpStatus.CREATED).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, discoveryResponse);
        } catch (Exception ex) {
            return ResponseEntityBuilder.getBuilder(HttpStatus.INTERNAL_SERVER_ERROR).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, "Unable to discover right now");
        }
    }

    @GetMapping(value = UrlConstants.DISCOVERY, consumes = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse> getAllServices() {
        log.info("Received a request to get all the discovered services");
        List<DiscoveryResponse> discoveryResponse = null;
        try {
            discoveryResponse = discoveryService.getAll();
            return ResponseEntityBuilder.getBuilder(HttpStatus.CREATED).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, discoveryResponse);
        } catch (Exception ex) {
            return ResponseEntityBuilder.getBuilder(HttpStatus.INTERNAL_SERVER_ERROR).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, "Unable to discover right now");
        }
    }


}
