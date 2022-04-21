package com.wscbs.group12.urlshortner.user.controller;

import com.wscbs.group12.urlshortner.user.constants.ApplicationConstants;
import com.wscbs.group12.urlshortner.user.constants.UrlConstants;
import com.wscbs.group12.urlshortner.user.exceptions.UserException;
import com.wscbs.group12.urlshortner.user.model.UserRequest;
import com.wscbs.group12.urlshortner.user.model.UserResponse;
import com.wscbs.group12.urlshortner.user.model.rest.ResponseEntityBuilder;
import com.wscbs.group12.urlshortner.user.model.rest.RestApiResponse;
import com.wscbs.group12.urlshortner.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;

    @PostMapping(value = UrlConstants.USER_CREATE, produces = ApplicationConstants.APPLICATION_JSON, consumes = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Received a request to create a new User- {}", userRequest);
        UserResponse isUserExist = null;
        try {
            isUserExist = userService.getUser(userRequest.getUserId());
        } catch (UserException ex) {
            isUserExist = null;
        }
        if (isUserExist != null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.IM_USED).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "UserID already exist");
        UserResponse response = userService.create(userRequest);
        if (response == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.BAD_REQUEST).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unable to create the User");
        return ResponseEntityBuilder.getBuilder(HttpStatus.OK).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, response);
    }

    @GetMapping(value = UrlConstants.USER_GET_BY_ID, produces = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse> getUserById(@PathVariable String userId) {
        log.info("Received a request to get the user- {}", userId);
        UserResponse user = userService.getUser(userId);
        if (user == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.NOT_FOUND).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "User object doesn't exist");
        return ResponseEntityBuilder.getBuilder(HttpStatus.OK).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, user);
    }

    @PutMapping(value = UrlConstants.USER_UPDATE, produces = ApplicationConstants.APPLICATION_JSON, consumes = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse> updateUser(@Valid @RequestBody UserRequest userRequest, @PathVariable String userId) {
        log.info("Received a request to update user request - {}, {}", userRequest, userId);
        UserResponse isObjectExist = userService.getUser(userId);
        if (isObjectExist == null)
            return ResponseEntityBuilder.getBuilder(HttpStatus.NOT_FOUND).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Object doesn't exist in the DB. Invalid ID");
        UserResponse response = userService.update(userId, userRequest);
        if (response == null)
            return ResponseEntityBuilder
                    .getBuilder(HttpStatus.BAD_REQUEST)
                    .errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unable to update the user data");
        return ResponseEntityBuilder
                .getBuilder(HttpStatus.OK)
                .successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, response);
    }

    @DeleteMapping(value = UrlConstants.USER_DELETE_BY_ID, produces = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse>
    deleteUserById(@PathVariable String userId) {
        log.info("Received a request to delete user Data : {}", userId);
        boolean isDeleted = userService.deleteById(userId);
        if (!isDeleted)
            return ResponseEntityBuilder
                    .getBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Unable to process your request");
        return ResponseEntityBuilder.getBuilder(HttpStatus.NO_CONTENT).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, isDeleted);
    }

    @DeleteMapping(value = UrlConstants.USER_DELETE_ALL, produces = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse>
    deleteAllUsers() {
        log.info("Received a request to delete all users");
        return ResponseEntityBuilder
                .getBuilder(HttpStatus.FORBIDDEN)
                .errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "The operation is forbidden");
    }
}
