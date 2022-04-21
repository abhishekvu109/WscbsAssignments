package com.wscbs.group12.urlshortner.user.config;

import com.wscbs.group12.urlshortner.user.constants.ApplicationConstants;
import com.wscbs.group12.urlshortner.user.exceptions.UserException;
import com.wscbs.group12.urlshortner.user.model.DiscoveryRequest;
import com.wscbs.group12.urlshortner.user.model.rest.ResponseEntityBuilder;
import com.wscbs.group12.urlshortner.user.model.rest.RestApiResponse;
import com.wscbs.group12.urlshortner.user.util.ErrorCode;
import com.wscbs.group12.urlshortner.user.util.ResponseErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiscoveryServiceRequestImpl {
    public static boolean isAck = false;
    private final RestTemplate restTemplate;
    @Value("${group12.discovery.service.url}")
    private String discoveryServiceUrl;
    @Value("${server.port}")
    private String port;
    @Value("${group12.discovery.service.gateways}")
    private String gateways;
    @Value("${app.base.uri}")
    private String baseUri;

    @Scheduled(fixedDelay = 15000)
    public void discover() {
        if (!isAck) {
            log.info("Preparing a request for discovery");
            List<String> apiGateways = getUrls();
            if (CollectionUtils.isEmpty(apiGateways))
                throw new UserException(ResponseErrorCode.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR, "User service isn't discovered");

            DiscoveryRequest discoveryRequest = DiscoveryRequest.builder()
                    .name(ApplicationConstants.APP_SERVICE_NAME)
                    .port(port)
                    .baseUri(baseUri)
                    .url(apiGateways)
                    .build();
            ResponseEntity<RestApiResponse> res = null;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<Object> entity = new HttpEntity<>(discoveryRequest, headers);
            try {
                res = this.restTemplate.postForEntity(discoveryServiceUrl, entity, RestApiResponse.class);
                if (res.getStatusCode() == HttpStatus.CREATED)
                    res = ResponseEntityBuilder.getBuilder(HttpStatus.CREATED).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, res.getBody());
                isAck = true;
            } catch (final HttpClientErrorException e) {
                res = ResponseEntityBuilder.getBuilder(e.getStatusCode()).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, e.getResponseBodyAsString());
            } catch (Exception ex) {
                res = ResponseEntityBuilder.getBuilder(HttpStatus.INTERNAL_SERVER_ERROR).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unable to process your request");
            }
            log.info("Response received from the Discovery server: {}", res);
        }
    }

    private List<String> getUrls() {
        List<String> urls = new LinkedList<>();
        if (gateways != null && !gateways.isEmpty()) {
            for (String s : gateways.split("\\|"))
                urls.add(s);

        }
        return urls;
    }


}
