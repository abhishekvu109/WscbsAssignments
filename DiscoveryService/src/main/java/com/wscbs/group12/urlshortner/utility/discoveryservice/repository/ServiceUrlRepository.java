package com.wscbs.group12.urlshortner.utility.discoveryservice.repository;

import com.wscbs.group12.urlshortner.utility.discoveryservice.entities.ServiceUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceUrlRepository extends JpaRepository<ServiceUrl, Long> {
    ServiceUrl findByUrl(String url);

}
