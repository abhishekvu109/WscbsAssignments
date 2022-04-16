package com.wscbs.group12.urlshortner.user.controller;

import com.wscbs.group12.urlshortner.user.constants.ApplicationConstants;
import com.wscbs.group12.urlshortner.user.constants.UrlConstants;
import com.wscbs.group12.urlshortner.user.model.LoginRequest;
import com.wscbs.group12.urlshortner.user.model.rest.ResponseEntityBuilder;
import com.wscbs.group12.urlshortner.user.model.rest.RestApiResponse;
import com.wscbs.group12.urlshortner.user.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginController {

    private final AuthenticationManager authManager;
    private final JWTUtil jwtUtil;

    @PostMapping(value = UrlConstants.USER_LOGIN, produces = ApplicationConstants.APPLICATION_JSON, consumes = ApplicationConstants.APPLICATION_JSON)
    public @ResponseBody
    ResponseEntity<RestApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword());
        Authentication authentication = null;
        try {
            authentication = authManager.authenticate(authInputToken);
            if (authentication.isAuthenticated()) {
                String token = jwtUtil.generateToken(loginRequest.getUserId());
                return ResponseEntityBuilder.getBuilder(HttpStatus.OK).successResponse(ApplicationConstants.REQUEST_SUCCESS_DESCRIPTION, Collections.singletonMap("jwt-token", token));
            }
        } catch (AuthenticationException ex) {
            return ResponseEntityBuilder.getBuilder(HttpStatus.UNAUTHORIZED).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "User is not authorized to login");
        }
        return ResponseEntityBuilder.getBuilder(HttpStatus.FORBIDDEN).errorResponse(ApplicationConstants.REQUEST_FAILURE_DESCRIPTION, "Some issue");
    }

}
