package com.wscbs.group12.urlshortner.utility.discoveryservice.repository;

import com.wscbs.group12.urlshortner.utility.discoveryservice.entities.Discovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscoveryRepository extends JpaRepository<Discovery, Long> {
    List<Discovery> findByServiceName(String name);

    Discovery findByServiceNameAndPort(String serviceName, int port);
}
