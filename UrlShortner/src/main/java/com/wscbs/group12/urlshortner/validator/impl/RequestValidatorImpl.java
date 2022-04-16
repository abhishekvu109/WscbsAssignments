package com.wscbs.group12.urlshortner.validator.impl;

import com.wscbs.group12.urlshortner.model.UrlShortenerRequest;
import com.wscbs.group12.urlshortner.validator.RequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RequestValidatorImpl implements RequestValidator {

    @Override
    public boolean isValidUrl(String url) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }

    @Override
    public boolean isValidDuration(int duration) {
        return duration > 45 && duration < 4320;
    }

    @Override
    public boolean isValidRequest(UrlShortenerRequest request) {
        return false;
    }
}
