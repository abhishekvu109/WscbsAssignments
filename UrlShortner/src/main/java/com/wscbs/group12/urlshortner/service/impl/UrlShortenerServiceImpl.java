package com.wscbs.group12.urlshortner.service.impl;

import com.wscbs.group12.urlshortner.entities.UrlShortenerEntity;
import com.wscbs.group12.urlshortner.exceptions.UrlShortenerException;
import com.wscbs.group12.urlshortner.model.UrlShortenerRequest;
import com.wscbs.group12.urlshortner.model.UrlShortenerResponse;
import com.wscbs.group12.urlshortner.repository.UrlShortenerRepository;
import com.wscbs.group12.urlshortner.service.UrlShortenerService;
import com.wscbs.group12.urlshortner.utils.ErrorCode;
import com.wscbs.group12.urlshortner.utils.ResponseErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private final UrlShortenerRepository urlShortenerRepository;
    private final UrlShortenerImplHelper urlShortenerImplHelper;

    @Override
    @Transactional
    public UrlShortenerResponse generateShortUrl(UrlShortenerRequest request) {
        UrlShortenerEntity shortenerEntity = urlShortenerImplHelper.buildUrlShortenerEntityFromRequest(request);
        shortenerEntity = urlShortenerRepository.saveAndFlush(shortenerEntity);
        return UrlShortenerResponse
                .builder()
                .url(shortenerEntity.getUrl())
                .shortUrl(shortenerEntity.getTinyUrl())
                .refId(shortenerEntity.getRefId())
                .expirationTimeStamp(shortenerEntity.getExpiryTime())
                .build();
    }

    @Override
    public UrlShortenerResponse getShortUrlByRefId(String refId) {
        UrlShortenerEntity shortenerEntity = urlShortenerRepository.findByRefId(refId);
        if (shortenerEntity == null) {
            log.error("Object not found - {}", refId);
            throw new UrlShortenerException(ResponseErrorCode.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR, "Object not found");
        }
        return urlShortenerImplHelper.buildResponseFromEntity(shortenerEntity);
    }

    @Override
    public List<UrlShortenerResponse> getShortUrlByUserId(String userId) {
        List<UrlShortenerEntity> shortenerEntities = urlShortenerRepository.findByUserId(userId);
        if (CollectionUtils.isEmpty(shortenerEntities)) {
            log.error("Object not found - {}", userId);
            throw new UrlShortenerException(ResponseErrorCode.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR, "Object not found");
        }
        return urlShortenerImplHelper.buildResponseListFromEntities(shortenerEntities);
    }

    @Override
    @Transactional
    public UrlShortenerResponse updateUrl(String refId, UrlShortenerRequest request) {
        UrlShortenerEntity urlShortenerEntity = urlShortenerRepository.findByRefId(refId);
        if (urlShortenerEntity == null) {
            log.error("Object not found - {}", refId);
            throw new UrlShortenerException(ResponseErrorCode.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR, "Object not found");
        }
        urlShortenerImplHelper.updateUrlShortenerEntityFromRequest(urlShortenerEntity, request);
        return urlShortenerImplHelper.buildResponseFromEntity(urlShortenerRepository.saveAndFlush(urlShortenerEntity));
    }

    @Override
    @Transactional
    public boolean deleteByRefId(String refId) {
        UrlShortenerEntity urlShortenerEntity = urlShortenerRepository.findByRefId(refId);
        if (urlShortenerEntity == null) {
            log.error("Object not found - {}", refId);
            throw new UrlShortenerException(ResponseErrorCode.UNKNOWN_ERROR, ErrorCode.UNKNOWN_ERROR, "Object not found");
        }
        urlShortenerRepository.delete(urlShortenerEntity);
        return true;
    }

    @Override
    public List<UrlShortenerResponse> getAllKeys() {
        List<UrlShortenerEntity> shortenerEntities = urlShortenerRepository.findAll();
        return urlShortenerImplHelper.buildResponseListFromEntities(shortenerEntities);
    }
}
