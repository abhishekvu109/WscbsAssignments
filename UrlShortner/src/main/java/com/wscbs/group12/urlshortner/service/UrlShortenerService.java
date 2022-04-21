package com.wscbs.group12.urlshortner.service;

import com.wscbs.group12.urlshortner.model.UrlShortenerRequest;
import com.wscbs.group12.urlshortner.model.UrlShortenerResponse;

import java.util.List;

public interface UrlShortenerService {
    UrlShortenerResponse generateShortUrl(UrlShortenerRequest request);

    UrlShortenerResponse getShortUrlByRefId(String refId);

    List<UrlShortenerResponse> getShortUrlByUserId(String userId);

    UrlShortenerResponse updateUrl(String refId, UrlShortenerRequest request);

    boolean deleteByRefId(String refId);

    boolean deleteByUserIdAndRefId(String userId, String refId);

    UrlShortenerResponse getShortUrlByUserIdAndRefId(String userId, String refId);

    List<UrlShortenerResponse> getAllKeys();

}
