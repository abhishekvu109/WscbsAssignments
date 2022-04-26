package com.wscbs.group12.urlshortner.utility.discoveryservice.repository;

import com.wscbs.group12.urlshortner.utility.discoveryservice.entities.ServiceUrl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceUrlRepository extends JpaRepository<ServiceUrl, Long> {
    List<ServiceUrl> findByUrl(String url, Pageable onlyOne);
    List<ServiceUrl> findByDiscoveryId(long discoveryId);
}
