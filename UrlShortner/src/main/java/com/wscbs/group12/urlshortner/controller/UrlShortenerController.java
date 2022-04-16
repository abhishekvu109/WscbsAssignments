package com.wscbs.group12.urlshortner.controller;

import com.wscbs.group12.urlshortner.constants.ApplicationConstants;
import com.wscbs.group12.urlshortner.constants.UrlConstants;
import com.wscbs.group12.urlshortner.entities.UserEntity;
import com.wscbs.group12.urlshortner.model.UrlShortenerRequest;
import com.wscbs.group12.urlshortner.model.UrlShortenerResponse;
import com.wscbs.group12.urlshortner.model.rest.ResponseEntityBuilder;
import com.wscbs.group12.urlshortner.model.rest.RestApiResponse;
import com.wscbs.group12.urlshortner.repository.UserRepository;
import com.wscbs.group12.urlshortner.service.UrlShortenerService;
import com.wscbs.group12.urlshortner.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UrlShortenerController {

  
    private final UrlShortenerService urlShortenerService;
    private final RequestValidator validator;
    private final UserRepository userRepository;

    @GetMapping(value = UrlConstants.URL_GET_BY_ID, produces = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse>
    getShortUrlById(@PathVariable String refId) {
        UserEntity userEntity = getAuthenticatedUser();
        if (userEntity == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.UNAUTHORIZED).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unauthorized access");
        log.info("Received a request to fetch Short Url Data : {}", refId);
        UrlShortenerResponse response = urlShortenerService.getShortUrlByRefId(refId);
        if (response == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.NOT_FOUND).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Object not found");
        return ResponseEntityBuilder.getBuilder(HttpStatus.MOVED_PERMANENTLY).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, response);
    }

    @GetMapping(value = UrlConstants.URL_GET_BY_USER, produces = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse>
    getShortUrlByUser(@PathVariable String userId) {
        UserEntity userEntity = getAuthenticatedUser();
        if (userEntity == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.UNAUTHORIZED).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unauthorized access");

        log.info("Received a request to fetch Short Url Data by UserId : {}", userId);
        List<UrlShortenerResponse> response = urlShortenerService.getShortUrlByUserId(userId);
        if (response == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.NOT_FOUND).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "No data found for the user");
        return ResponseEntityBuilder.getBuilder(HttpStatus.OK).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, response);
    }

    @GetMapping(value = UrlConstants.URL_GET_ALL_KEYS, produces = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse>
    getAllShortUrls() {
        UserEntity userEntity = getAuthenticatedUser();
        if (userEntity == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.UNAUTHORIZED).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unauthorized access");

        log.info("Received a request to fetch all the Short Url Data ");
        List<UrlShortenerResponse> response = urlShortenerService.getAllKeys();
        if (response == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.INTERNAL_SERVER_ERROR).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unable to process the request");
        return ResponseEntityBuilder.getBuilder(HttpStatus.OK).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, response);
    }

    @PostMapping(value = UrlConstants.URL_CREATE, produces = ApplicationConstants.APPLICATION_JSON, consumes = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse> generateShortUrl(@Valid @RequestBody UrlShortenerRequest urlShortenerRequest) {
        UserEntity userEntity = getAuthenticatedUser();
        if (userEntity == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.UNAUTHORIZED).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unauthorized access");
        urlShortenerRequest.setUserId(userEntity.getUserId());
        log.info("Received a request to register a new Short Urls- {} , {}", urlShortenerRequest, userEntity);
        if (!validator.isValidUrl(urlShortenerRequest.getUrl()))
            return ResponseEntityBuilder.getBuilder(HttpStatus.BAD_REQUEST).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Invalid URL format");
        UrlShortenerResponse response = urlShortenerService.generateShortUrl(urlShortenerRequest);
        if (response == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.BAD_REQUEST).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unable to create the short url");
        return ResponseEntityBuilder.getBuilder(HttpStatus.CREATED).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, response);
    }

    @PutMapping(value = UrlConstants.URL_UPDATE, produces = ApplicationConstants.APPLICATION_JSON, consumes = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse> generateShortUrl(@Valid @RequestBody UrlShortenerRequest urlShortenerRequest, @PathVariable String refId) {
        UserEntity userEntity = getAuthenticatedUser();
        if (userEntity == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.UNAUTHORIZED).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unauthorized access");

        log.info("Received a request to update Url Shortener request - {}, {}", urlShortenerRequest, refId);
        UrlShortenerResponse isObjectExist = urlShortenerService.getShortUrlByRefId(refId);
        if (!validator.isValidUrl(urlShortenerRequest.getUrl()))
            return ResponseEntityBuilder.getBuilder(HttpStatus.BAD_REQUEST).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Invalid URL format");
        if (isObjectExist == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.NOT_FOUND).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Object doesn't exist in the DB. Invalid ID");
        UrlShortenerResponse response = urlShortenerService.updateUrl(refId, urlShortenerRequest);
        if (response == null)
            return ResponseEntityBuilder
                    .getBuilder(HttpStatus.BAD_REQUEST)
                    .errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unable to update the short url");
        return ResponseEntityBuilder
                .getBuilder(HttpStatus.OK)
                .successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, response);
    }

    @DeleteMapping(value = UrlConstants.URL_DELETE_BY_ID, produces = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse>
    deleteShortUrlById(@PathVariable String refId) {
        UserEntity userEntity = getAuthenticatedUser();
        if (userEntity == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.UNAUTHORIZED).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unauthorized access");

        log.info("Received a request to delete Short Url Data : {}", refId);
        boolean isDeleted = urlShortenerService.deleteByRefId(refId);
        if (!isDeleted)
            return ResponseEntityBuilder
                    .getBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unable to process your request");
        return ResponseEntityBuilder.getBuilder(HttpStatus.NO_CONTENT).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, isDeleted);
    }

    @DeleteMapping(value = UrlConstants.URL_DELETE_ALL, produces = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse>
    deleteAllShortUrls() {
        UserEntity userEntity = getAuthenticatedUser();
        if (userEntity == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.UNAUTHORIZED).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unauthorized access");
        
        log.info("Received a request to delete all Short Urls");
        return ResponseEntityBuilder
                .getBuilder(HttpStatus.FORBIDDEN)
                .errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "The operation is forbidden");
    }


    private UserEntity getAuthenticatedUser() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("UserId is: " + userId);
        return userRepository.findByUserId(userId);
    }
}
