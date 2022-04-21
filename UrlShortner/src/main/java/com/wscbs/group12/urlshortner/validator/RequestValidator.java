package com.wscbs.group12.urlshortner.validator;

import com.wscbs.group12.urlshortner.model.UrlShortenerRequest;

public interface RequestValidator {
    boolean isValidUrl(String url);

    boolean isValidDuration(int duration);

    boolean isValidRequest(UrlShortenerRequest request);
}
