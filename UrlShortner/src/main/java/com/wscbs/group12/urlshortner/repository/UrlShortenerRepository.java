package com.wscbs.group12.urlshortner.repository;

import com.wscbs.group12.urlshortner.entities.UrlShortenerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlShortenerRepository extends JpaRepository<UrlShortenerEntity, Long> {
    UrlShortenerEntity findByRefId(String refId);

    UrlShortenerEntity findByUrlHash(String urlHash);

    List<UrlShortenerEntity> findByUserId(String userid);
}
