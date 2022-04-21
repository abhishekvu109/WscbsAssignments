package com.wscbs.group12.urlshortner.service.impl;

import com.wscbs.group12.urlshortner.constants.ApplicationConstants;
import com.wscbs.group12.urlshortner.entities.UrlShortenerEntity;
import com.wscbs.group12.urlshortner.model.UrlShortenerRequest;
import com.wscbs.group12.urlshortner.model.UrlShortenerResponse;
import com.wscbs.group12.urlshortner.repository.UrlShortenerRepository;
import com.wscbs.group12.urlshortner.service.classengine.*;
import com.wscbs.group12.urlshortner.utils.CommonUtil;
import com.wscbs.group12.urlshortner.utils.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UrlShortenerImplHelper {

    private final CommonUtil commonUtil;
    private final UrlShortenerRepository urlShortenerRepository;

    public UrlShortenerEntity buildUrlShortenerEntityFromRequest(UrlShortenerRequest urlShortenerRequest) {
        String input = urlShortenerRequest.getUrl() + urlShortenerRequest.getUserId() + LocalDateTime.now();
        String hash = commonUtil.getBase64Encoding(input);
        String urlHash = commonUtil.getBase64Encoding(urlShortenerRequest.getUrl() + urlShortenerRequest.getUserId());
        UrlShortenerEntity shortenerEntity = urlShortenerRepository.findByUrlHash(urlHash);
        if (shortenerEntity != null && LocalDateTime.now().isBefore(shortenerEntity.getExpiryTime()))
            return shortenerEntity;
        Pair<String, String> keyAndClass = getKey(hash, ApplicationConstants.KEY_LENGTH);
        return UrlShortenerEntity.builder()
                .uuid(UUID.randomUUID().toString())
                .url(urlShortenerRequest.getUrl())
                .refId(keyAndClass.getKey())
                .duration(urlShortenerRequest.getDuration())
                .expiryTime(LocalDateTime.now().plusMinutes(urlShortenerRequest.getDuration()))
                .classGen(keyAndClass.getValue())
                .tinyUrl(ApplicationConstants.DOMAIN_NAME + keyAndClass.getKey())
                .userId(urlShortenerRequest.getUserId())
                .urlHash(urlHash)
                .build();
    }

    public UrlShortenerEntity updateUrlShortenerEntityFromRequest(UrlShortenerEntity urlShortenerEntity, UrlShortenerRequest urlShortenerRequest) {
        String input = urlShortenerRequest.getUrl() + urlShortenerRequest.getUserId() + LocalDateTime.now();
        String hash = commonUtil.getBase64Encoding(input);
        String urlHash = commonUtil.getBase64Encoding(urlShortenerRequest.getUrl() + urlShortenerRequest.getUserId());
        UrlShortenerEntity shortenerEntity = urlShortenerRepository.findByUrlHash(urlHash);
        if (shortenerEntity != null && LocalDateTime.now().isBefore(shortenerEntity.getExpiryTime()))
            return shortenerEntity;
        Pair<String, String> keyAndClass = getKey(hash, ApplicationConstants.KEY_LENGTH);
        urlShortenerEntity.setUrl(urlShortenerRequest.getUrl());
        urlShortenerEntity.setRefId(keyAndClass.getKey());
        urlShortenerEntity.setDuration(urlShortenerRequest.getDuration());
        urlShortenerEntity.setExpiryTime(LocalDateTime.now().plusMinutes(urlShortenerRequest.getDuration()));
        urlShortenerEntity.setTinyUrl(ApplicationConstants.DOMAIN_NAME + keyAndClass.getKey());
        urlShortenerEntity.setUrlHash(urlHash);
        return urlShortenerEntity;
    }

    private Pair<String, String> getKey(String input, int len) {
        ClassEngine classEngine = new ClassA();
        String key = classEngine.geyKey(input, len);
        Pair<String, String> pair = new Pair<>(key, "A");
        UrlShortenerEntity urlShortenerEntity = urlShortenerRepository.findByRefId(key);
        if (urlShortenerEntity != null) {
            classEngine = new ClassB();
            key = classEngine.geyKey(input, len);
            pair = new Pair<>(key, "B");
            urlShortenerEntity = urlShortenerRepository.findByRefId(key);
            if (urlShortenerEntity != null) {
                classEngine = new ClassC();
                key = classEngine.geyKey(input, len);
                pair = new Pair<>(key, "C");
                urlShortenerEntity = urlShortenerRepository.findByRefId(key);
                if (urlShortenerEntity != null) {
                    classEngine = new ClassD();
                    key = classEngine.geyKey(input, len);
                    pair = new Pair<>(key, "D");
                    urlShortenerEntity = urlShortenerRepository.findByRefId(key);
                    while (urlShortenerEntity != null) {
                        key = classEngine.geyKey(input, len);
                        pair = new Pair<>(key, "D");
                        urlShortenerEntity = urlShortenerRepository.findByRefId(key);
                    }
                    return pair;
                }
                return pair;
            }
            return pair;
        }
        return pair;
    }

    public List<UrlShortenerResponse> buildResponseListFromEntities(List<UrlShortenerEntity> entities) {
        List<UrlShortenerResponse> responses = new LinkedList<>();
        for (UrlShortenerEntity shortenerEntity : entities) {
            UrlShortenerResponse response = UrlShortenerResponse
                    .builder()
                    .url(shortenerEntity.getUrl())
                    .shortUrl(shortenerEntity.getTinyUrl())
                    .refId(shortenerEntity.getRefId())
                    .expirationTimeStamp(shortenerEntity.getExpiryTime())
                    .build();
            responses.add(response);
        }
        return responses;
    }

    public UrlShortenerResponse buildResponseFromEntity(UrlShortenerEntity shortenerEntity) {
        return UrlShortenerResponse
                .builder()
                .url(shortenerEntity.getUrl())
                .shortUrl(shortenerEntity.getTinyUrl())
                .refId(shortenerEntity.getRefId())
                .expirationTimeStamp(shortenerEntity.getExpiryTime())
                .build();
    }

}
